package com.audioburst.library.utils

import com.audioburst.library.models.InternalPlaybackState

internal fun playbackStateOf(
    url: String = "",
    position: Double = 0.0,
    occurrenceTime: Long = 0,
) : InternalPlaybackState =
    InternalPlaybackState(
        url = url,
        position = position,
        occurrenceTime = occurrenceTime,
    )
