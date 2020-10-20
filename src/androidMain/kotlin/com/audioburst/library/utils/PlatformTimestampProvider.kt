package com.audioburst.library.utils

actual object PlatformTimestampProvider : TimestampProvider {

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
