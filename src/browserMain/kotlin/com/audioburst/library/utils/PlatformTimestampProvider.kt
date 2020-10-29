package com.audioburst.library.utils

import kotlin.js.Date

internal actual object PlatformTimestampProvider : TimestampProvider {

    override fun currentTimeMillis(): Long = Date.now().toLong()
}
