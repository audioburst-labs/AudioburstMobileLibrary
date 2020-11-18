package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.result
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class ObservePersonalPlaylist(
    private val getUser: GetUser,
    private val personalPlaylistRepository: PersonalPlaylistRepository,
) {

    operator fun invoke(): Flow<Result<PendingPlaylist>> = flow {
        val userResource = getUser()
        if (userResource is Resource.Error) {
            emit(userResource)
            return@flow
        }
        val user = userResource.result()!!

        val personalPlaylistQueryIdResource = personalPlaylistRepository.getPersonalPlaylistQueryId(user)
        if (personalPlaylistQueryIdResource is Resource.Error) {
            emit(personalPlaylistQueryIdResource)
            return@flow
        }
        val personalPlaylistQueryId = personalPlaylistQueryIdResource.result()!!

        loop@ while (true) {
            when (val pendingPlaylist = personalPlaylistRepository.getPersonalPlaylist(user, personalPlaylistQueryId)) {
                is Resource.Data -> {
                    emit(pendingPlaylist)
                    if (pendingPlaylist.result.isReady) {
                        break@loop
                    } else {
                        delay()
                    }
                }
                is Resource.Error -> {
                    emit(pendingPlaylist)
                    break@loop
                }
            }
        }
    }
        .distinctUntilChanged { old: Resource<PendingPlaylist>, new: Resource<PendingPlaylist> ->
            if (old is Resource.Data && new is Resource.Data) {
                old.result.playlist.bursts.map { it.id } == new.result.playlist.bursts.map { it.id } && old.result.isReady == new.result.isReady
            } else {
                old == new
            }
        }.map { it.asResult() }

    private suspend fun delay() = delay(TIME_BETWEEN_CALLS)

    companion object {
        private const val TIME_BETWEEN_CALLS = 1000L
    }
}
