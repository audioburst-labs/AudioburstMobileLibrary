package com.audioburst.library.utils

interface Settings {
    fun getStringOrNull(key: String): String?

    fun putString(key: String, value: String?)

    fun getIntOrDefault(key: String, default: Int): Int

    fun putInt(key: String, value: Int)

    fun getBooleanOrDefault(key: String, default: Boolean): Boolean

    fun putBoolean(key: String, value: Boolean)
}

expect fun createSettings(name: String): Settings