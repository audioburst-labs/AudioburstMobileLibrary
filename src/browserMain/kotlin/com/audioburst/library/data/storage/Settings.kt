package com.audioburst.library.data.storage

import com.russhwolf.settings.JsSettings
import com.russhwolf.settings.Settings

internal actual fun createSettings(name: String): Settings = JsSettings()