package com.audioburst.library.models

internal data class EventPayload(
    val playlistName: String,
    val playlistId: Int,
    val burst: Burst,
    val currentPlayBackPosition: Long,
    val playerSessionId: PlayerSessionId
)
