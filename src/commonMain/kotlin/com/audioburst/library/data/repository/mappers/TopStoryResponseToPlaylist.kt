package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.PlayerSessionId
import com.audioburst.library.models.Playlist
import com.audioburst.library.utils.PlayerSessionIdGetter

internal class TopStoryResponseToPlaylist(
    private val burstResponseToBurstMapper: BurstResponseToBurstMapper,
    private val playerSessionIdGetter: PlayerSessionIdGetter,
) {

    fun map(
        from: TopStoryResponse,
        userId: String,
        playlistId: String,
        playlistName: String,
        playerAction: PlayerAction,
        playerSessionId: PlayerSessionId? = null,
    ): Playlist =
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
            playerSessionId = playerSessionId ?: playerSessionIdGetter.get(),
            playerAction = playerAction,
            intent = Playlist.Intent.create(from.intent),
        )
}
