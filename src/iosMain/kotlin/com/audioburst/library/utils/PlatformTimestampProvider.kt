package com.audioburst.library.utils

import com.audioburst.library.models.Duration
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

internal actual object PlatformTimestampProvider : TimestampProvider {

    override fun timeSince1970(): Duration = NSDate().timeIntervalSince1970.toDuration(DurationUnit.Seconds)
}