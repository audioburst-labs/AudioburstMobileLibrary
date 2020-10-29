package com.audioburst.library.interactors

import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.then
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.Result

internal class GetPlaylistsInfo(
    private val getUser: GetUser,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): Result<List<PlaylistInfo>> =
        getUser().then { user ->
            userRepository.getPlaylists(userId = user.userId)
        }.asResult()
}
