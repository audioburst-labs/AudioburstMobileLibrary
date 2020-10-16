package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo

internal class GetPlaylist(
    private val getUser: GetUser,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(playlistInfo: PlaylistInfo): Resource<Playlist> =
        getUser() then { user ->
            userRepository.getPlaylist(
                playlistInfo = playlistInfo,
                userId = user.userId,
            )
        }
}
