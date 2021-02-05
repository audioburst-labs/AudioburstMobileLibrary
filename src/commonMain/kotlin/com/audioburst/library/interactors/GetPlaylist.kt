package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.asResult
import com.audioburst.library.data.onData
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.Result
import com.audioburst.library.models.User

internal class GetPlaylist(
    private val getUser: GetUser,
    private val userRepository: UserRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
) {

    suspend operator fun invoke(playlistInfo: PlaylistInfo): Result<Playlist> =
        getPlaylist { userRepository.getPlaylist(it.userId, playlistInfo) }

    suspend operator fun invoke(byteArray: ByteArray): Result<Playlist> =
        getPlaylist { userRepository.getPlaylist(it.userId, byteArray) }

    private suspend fun getPlaylist(getPlaylistCall: suspend (User) -> Resource<Playlist>): Result<Playlist> =
        getUser().then { user ->
            getPlaylistCall(user).onData {
                postContentLoadEvent(it)
            }
        }.asResult()
}
