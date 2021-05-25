package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.asResult
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Result

internal class Search(
    private val getUser: GetUser,
    private val userRepository: UserRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
    private val playlistStorage: PlaylistStorage,
) {

    suspend operator fun invoke(query: String): Result<Playlist> =
        when (val getUserResult = getUser()) {
            is Resource.Data -> when (val resource = userRepository.search(userId = getUserResult.result.userId, query = query)) {
                is Resource.Data -> {
                    if (resource.result.bursts.isNotEmpty()) {
                        postContentLoadEvent(resource.result)
                        playlistStorage.setPlaylist(resource.result)
                        resource.asResult()
                    } else {
                        Result.Error(LibraryError.NoSearchResults)
                    }
                }
                is Resource.Error -> resource.asResult()
            }
            is Resource.Error -> getUserResult.asResult()
        }
}
