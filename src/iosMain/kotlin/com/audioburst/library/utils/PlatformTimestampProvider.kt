package com.audioburst.library.utils

import platform.Foundation.NSDate
import platform.Foundation.now
import platform.Foundation.timeIntervalSince1970

actual object PlatformTimestampProvider : TimestampProvider {

    override fun currentTimeMillis(): Long = NSDate.now().timeIntervalSince1970.toLong()
}