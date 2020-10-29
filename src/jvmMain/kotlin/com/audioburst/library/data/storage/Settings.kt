package com.audioburst.library.data.storage

import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

internal actual fun createSettings(name: String): Settings = JvmPreferencesSettings(Preferences.userRoot())
