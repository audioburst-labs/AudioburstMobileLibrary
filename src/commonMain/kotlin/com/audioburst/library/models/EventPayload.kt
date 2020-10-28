package com.audioburst.library.models

internal data class EventPayload(
    val playlistName: String,
    val playlistId: Int,
    val burst: Burst,
    val isPlaying: Boolean,
    val occurrenceTime: Long,
    val currentPlayBackPosition: Duration,
    val playerSessionId: PlayerSessionId
)
