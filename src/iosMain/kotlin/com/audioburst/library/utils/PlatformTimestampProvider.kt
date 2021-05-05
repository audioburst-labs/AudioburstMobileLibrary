package com.audioburst.library.utils

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual object PlatformTimestampProvider : TimestampProvider {

    override fun currentTimeMillis(): Long = NSDate().timeIntervalSince1970.toLong() * 1000
}