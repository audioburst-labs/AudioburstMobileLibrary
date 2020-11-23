package com.audioburst.library.models

internal data class EventPayload(
    val playerAction: PlayerAction,
    val playlistName: String,
    val playlistId: String,
    val burst: Burst,
    val isPlaying: Boolean,
    val occurrenceTime: Long,
    val currentPlayBackPosition: Duration,
    val playerSessionId: PlayerSessionId
)
