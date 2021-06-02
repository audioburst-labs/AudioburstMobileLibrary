package com.audioburst.library.interactors

import com.audioburst.library.data.*
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.ListenedBurstStorage
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.Result
import com.audioburst.library.models.User

internal class GetPlaylist(
    private val getUser: GetUser,
    private val userRepository: UserRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
    private val playlistStorage: PlaylistStorage,
    private val listenedBurstStorage: ListenedBurstStorage,
    private val userStorage: UserStorage,
) {

    suspend operator fun invoke(playlistInfo: PlaylistInfo): Result<Playlist> =
        getPlaylist { userRepository.getPlaylist(it.userId, playlistInfo) }

    private suspend fun getPlaylist(getPlaylistCall: suspend (User) -> Resource<Playlist>): Result<Playlist> =
        getUser().then { user ->
            getPlaylistCall(user).onData {
                postContentLoadEvent(it)
            }
        }.filterListenedBursts().onData(playlistStorage::setPlaylist).asResult()

    private suspend fun Resource<Playlist>.filterListenedBursts(): Resource<Playlist> =
        if (userStorage.filterListenedBursts) {
            map { playlist ->
                val listenedBursts = listenedBurstStorage.getRecentlyListened().map { it.id }
                if (listenedBursts.isEmpty()) {
                    playlist
                } else {
                    val filteredBursts = playlist.bursts.filterNot { listenedBursts.contains(it.id) }
                    val newBurstList = filteredBursts.ifEmpty {
                        playlist.bursts.take(NUMBER_OF_BURST_WHEN_ALL_LISTENED)
                    }
                    playlist.copy(newBurstList)
                }
            }
        } else {
            this
        }

    companion object {
        private const val NUMBER_OF_BURST_WHEN_ALL_LISTENED = 2
    }
}
