package com.audioburst.library.utils

internal actual object PlatformTimestampProvider : TimestampProvider {

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
