package com.audioburst.library.data.storage

import android.content.Context
import com.audioburst.library.applicationContext
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings

actual fun createSettings(name: String): Settings = AndroidSettings(
    delegate = applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
)
