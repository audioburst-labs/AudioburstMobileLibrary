package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.result
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

internal class ObservePersonalPlaylist(
    private val getUser: GetUser,
    private val personalPlaylistRepository: PersonalPlaylistRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
    private val userStorage: UserStorage,
) {

    operator fun invoke(): Flow<Result<PendingPlaylist>> = flow {
        if (userStorage.selectedKeysCount == 0) {
            emit(Result.Error(LibraryError.NoKeysSelected))
            return@flow
        }

        val userResource = getUser()
        if (userResource is Resource.Error) {
            emit(resource = userResource)
            return@flow
        }
        val user = userResource.result()!!

        val personalPlaylistQueryIdResource = personalPlaylistRepository.getPersonalPlaylistQueryId(user)
        if (personalPlaylistQueryIdResource is Resource.Error) {
            emit(resource = personalPlaylistQueryIdResource)
            return@flow
        }
        val personalPlaylistQueryId = personalPlaylistQueryIdResource.result()!!

        loop@ while (true) {
            when (val pendingPlaylist = personalPlaylistRepository.getPersonalPlaylist(user, personalPlaylistQueryId)) {
                is Resource.Data -> {
                    emit(resource = pendingPlaylist)
                    if (pendingPlaylist.result.isReady) {
                        postContentLoadEvent(pendingPlaylist.result.playlist)
                        break@loop
                    } else {
                        delay()
                    }
                }
                is Resource.Error -> {
                    emit(resource = pendingPlaylist)
                    break@loop
                }
            }
        }
    }
        .distinctUntilChanged { old: Result<PendingPlaylist>, new: Result<PendingPlaylist> ->
            if (old is Result.Data && new is Result.Data) {
                old.value.playlist.bursts.map { it.id } == new.value.playlist.bursts.map { it.id } && old.value.isReady == new.value.isReady
            } else {
                old == new
            }
        }

    private suspend fun FlowCollector<Result<PendingPlaylist>>.emit(resource: Resource<PendingPlaylist>) {
        emit(resource.asResult())
    }

    private suspend fun delay() = delay(TIME_BETWEEN_CALLS)

    companion object {
        private const val TIME_BETWEEN_CALLS = 1000L
    }
}
