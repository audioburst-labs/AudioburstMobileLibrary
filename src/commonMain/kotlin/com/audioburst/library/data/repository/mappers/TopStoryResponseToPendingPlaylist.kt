package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.PendingPlaylist

internal class TopStoryResponseToPendingPlaylist constructor(
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
) {

    fun map(from: TopStoryResponse, userId: String): PendingPlaylist =
        PendingPlaylist(
            isReady = from.message == DONE_MESSAGE,
            playlist = topStoryResponseToPlaylist.map(
                from = from,
                userId = userId,
                playlistId = userId,
                playlistName = from.actualQuery ?: from.query ?: "",
            )
        )

    companion object {
        private const val DONE_MESSAGE = "DONE"
    }
}
