package com.audioburst.library.utils

internal fun interface TimestampProvider {
    fun currentTimeMillis(): Long
}

internal expect object PlatformTimestampProvider : TimestampProvider