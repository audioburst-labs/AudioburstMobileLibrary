package com.audioburst.library.utils

fun interface TimestampProvider {
    fun currentTimeMillis(): Long
}

expect object PlatformTimestampProvider : TimestampProvider