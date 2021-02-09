package com.audioburst.library.utils

interface Settings {
    fun getStringOrNull(key: String): String?

    fun putString(key: String, value: String?)
}

expect fun createSettings(name: String): Settings