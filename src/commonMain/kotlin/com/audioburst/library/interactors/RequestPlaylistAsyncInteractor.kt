package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.result
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

internal fun interface RequestPlaylistAsync {

    operator fun invoke(getPlaylistCall: suspend (User) -> Resource<PlaylistResult>): Flow<Result<PendingPlaylist>>
}

internal class RequestPlaylistAsyncInteractor(
    private val personalPlaylistRepository: PersonalPlaylistRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
    private val playlistStorage: PlaylistStorage,
    private val getUser: GetUser,
    private val asyncCallInterval: Duration = 1.0.toDuration(DurationUnit.Seconds)
) : RequestPlaylistAsync {

    override operator fun invoke(getPlaylistCall: suspend (User) -> Resource<PlaylistResult>): Flow<Result<PendingPlaylist>> = flow {
        val userResource = getUser()
        if (userResource is Resource.Error) {
            emit(resource = userResource)
            return@flow
        }
        val user = userResource.result()!!

        val async = when (val response = getPlaylistCall(user)) {
            is Resource.Data -> when (val result = response.result) {
                is PlaylistResult.Async -> result
                is PlaylistResult.Finished -> {
                    emit(resource = Resource.Data(PendingPlaylist.ready(result.value)))
                    return@flow
                }
            }
            is Resource.Error -> {
                emit(resource = response)
                return@flow
            }
        }
        val playlistQueryId = async.queryId

        loop@ while (true) {
            when (val pendingPlaylist = personalPlaylistRepository.getPersonalPlaylist(user, async.playerAction, async.playerSessionId, playlistQueryId)) {
                is Resource.Data -> {
                    Logger.i("Requesting Playlist Async, id: $playlistQueryId, status: success")
                    emit(resource = pendingPlaylist)
                    if (pendingPlaylist.result.isReady) {
                        break@loop
                    } else {
                        delay()
                    }
                }
                is Resource.Error -> {
                    Logger.i("Requesting Playlist Async, id: $playlistQueryId, status: error")
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
        .onEach { result: Result<PendingPlaylist> ->
            result.onData {
                if (it.isReady) {
                    postContentLoadEvent(it.playlist)
                }
                playlistStorage.setPlaylist(it.playlist)
            }
        }

    private suspend fun FlowCollector<Result<PendingPlaylist>>.emit(resource: Resource<PendingPlaylist>) {
        emit(resource.asResult())
    }

    private suspend fun delay() = delay(timeMillis = asyncCallInterval.milliseconds.toLong())
}
