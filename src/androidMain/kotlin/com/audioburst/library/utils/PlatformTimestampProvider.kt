package com.audioburst.library.utils

import com.audioburst.library.models.Duration
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration

internal actual object PlatformTimestampProvider : TimestampProvider {

    override fun timeSince1970(): Duration = System.currentTimeMillis().toDouble().toDuration(DurationUnit.Milliseconds)
}
