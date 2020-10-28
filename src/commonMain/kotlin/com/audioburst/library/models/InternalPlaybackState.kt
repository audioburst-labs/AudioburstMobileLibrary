package com.audioburst.library.models

data class PlaybackState(
    val url: String,
    val positionMillis: Long,
)

internal data class InternalPlaybackState(
    val url: String,
    val position: Double,
    val occurrenceTime: Long,
)
