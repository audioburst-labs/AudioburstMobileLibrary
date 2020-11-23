package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.Playlist
import com.audioburst.library.utils.PlayerSessionIdGetter

internal class TopStoryResponseToPlaylist constructor(
    private val burstResponseToBurstMapper: BurstResponseToBurstMapper,
    private val playerSessionIdGetter: PlayerSessionIdGetter,
) {

    fun map(from: TopStoryResponse, userId: String, playlistId: String, playlistName: String, playerAction: PlayerAction): Playlist =
        Playlist(
            id = playlistId,
            name = playlistName,
            query = from.actualQuery ?: "",
            bursts = from.bursts?.map {
                burstResponseToBurstMapper.map(
                    from = it,
                    userId = userId,
                    queryId = from.queryID
                )
            } ?: emptyList(),
            playerSessionId = playerSessionIdGetter.get(),
            playerAction = playerAction,
        )
}
