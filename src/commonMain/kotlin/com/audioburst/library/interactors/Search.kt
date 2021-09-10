package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.PlaylistRepository
import com.audioburst.library.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class Search(
    private val playlistRepository: PlaylistRepository,
    private val requestPlaylistAsync: RequestPlaylistAsync,
) {

    operator fun invoke(byteArray: ByteArray): Flow<Result<PendingPlaylist>> =
        getPlaylist { playlistRepository.search(userId = it.userId, byteArray = byteArray) }

    operator fun invoke(query: String): Flow<Result<PendingPlaylist>> =
        getPlaylist { playlistRepository.search(userId = it.userId, query = query) }

    private fun getPlaylist(getPlaylistCall: suspend (User) -> Resource<PlaylistResult>): Flow<Result<PendingPlaylist>> =
        requestPlaylistAsync { getPlaylistCall(it) }.map { result ->
            if (result is Result.Data && result.value.isReady && result.value.playlist.bursts.isEmpty()) {
                Result.Error(LibraryError.NoSearchResults)
            } else {
                result
            }
        }
}
