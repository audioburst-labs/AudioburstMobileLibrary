package com.audioburst.library.models

internal sealed class PlaylistResult {

    data class Finished(val value: Playlist) : PlaylistResult()

    data class Async(
        val queryId: PlaylistQueryId,
        val playerSessionId: PlayerSessionId,
        val playerAction: PlayerAction,
    ) : PlaylistResult()
}