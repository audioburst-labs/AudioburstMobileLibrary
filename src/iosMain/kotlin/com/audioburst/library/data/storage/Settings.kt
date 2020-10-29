package com.audioburst.library.data.storage

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings

internal actual fun createSettings(name: String): Settings = AppleSettings.Factory().create(name = name)