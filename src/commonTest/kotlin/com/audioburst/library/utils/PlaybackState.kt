package com.audioburst.library.utils

import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.models.toDuration

internal fun playbackStateOf(
    url: String = "",
    position: Double = 0.0,
    occurrenceTime: Long = 0,
) : InternalPlaybackState =
    InternalPlaybackState(
        url = url,
        position = position.toDuration(DurationUnit.Seconds),
        occurrenceTime = occurrenceTime,
    )
