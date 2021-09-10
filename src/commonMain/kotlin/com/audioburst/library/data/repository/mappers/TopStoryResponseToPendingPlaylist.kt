package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.PlayerSessionId

internal class TopStoryResponseToPendingPlaylist(
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
) {

    fun map(
        from: TopStoryResponse,
        userId: String,
        playerAction: PlayerAction,
        playerSessionId: PlayerSessionId,
    ): PendingPlaylist =
        PendingPlaylist(
            isReady = from.message == DONE_MESSAGE,
            playlist = topStoryResponseToPlaylist.map(
                from = from,
                userId = userId,
                playlistId = from.queryID.toString(),
                playlistName = from.actualQuery ?: from.query ?: "",
                playerAction = playerAction,
                playerSessionId = playerSessionId,
            )
        )

    companion object {
        private const val DONE_MESSAGE = "DONE"
    }
}
