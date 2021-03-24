package com.audioburst.library.utils

internal interface Settings {
    fun getStringOrNull(key: String): String?

    fun putString(key: String, value: String?)

    fun getIntOrDefault(key: String, default: Int): Int

    fun putInt(key: String, value: Int)

    fun getBooleanOrDefault(key: String, default: Boolean): Boolean

    fun putBoolean(key: String, value: Boolean)
}

internal expect fun createSettings(name: String): Settings