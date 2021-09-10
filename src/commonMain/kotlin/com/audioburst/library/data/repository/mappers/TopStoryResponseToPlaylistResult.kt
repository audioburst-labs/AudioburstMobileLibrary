package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.PlayerSessionId
import com.audioburst.library.models.PlaylistQueryId
import com.audioburst.library.models.PlaylistResult

internal class TopStoryResponseToPlaylistResult(
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
) {

    fun map(from: TopStoryResponse, userId: String, playerSessionId: PlayerSessionId, playerAction: PlayerAction): PlaylistResult =
        when (from.type) {
            ASYNC_TYPE -> PlaylistResult.Async(
                playerSessionId = playerSessionId,
                queryId = PlaylistQueryId(from.queryID),
                playerAction = playerAction,
            )
            else -> PlaylistResult.Finished(
                value = topStoryResponseToPlaylist.map(
                    from = from,
                    userId = userId,
                    playerAction = playerAction,
                    playerSessionId = playerSessionId,
                    playlistId = from.queryID.toString(),
                    playlistName = from.actualQuery ?: from.query ?: "",
                )
            )
        }

    companion object {
        private const val ASYNC_TYPE = "async"
    }
}