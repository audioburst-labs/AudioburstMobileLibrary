package com.audioburst.library.utils

import com.audioburst.library.models.PlaybackState

internal fun playbackStateOf(
    url: String = "",
    position: Double = 0.0,
    occurrenceTime: Long = 0,
) : PlaybackState =
    PlaybackState(
        url = url,
        position = position,
        occurrenceTime = occurrenceTime,
    )
