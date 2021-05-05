package com.audioburst.library.utils

import com.audioburst.library.models.Duration

internal fun interface TimestampProvider {
    fun timeSince1970(): Duration
}

internal expect object PlatformTimestampProvider : TimestampProvider