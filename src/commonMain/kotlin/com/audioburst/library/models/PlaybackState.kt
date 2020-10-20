package com.audioburst.library.models

internal data class PlaybackState(
    val url: String,
    val position: Double,
    val occurrenceTime: Long,
)
