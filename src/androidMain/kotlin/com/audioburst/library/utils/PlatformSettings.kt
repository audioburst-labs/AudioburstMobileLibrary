package com.audioburst.library.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.audioburst.library.applicationContext

actual fun createSettings(name: String): Settings = AndroidSettings(applicationContext.getSharedPreferences(name, MODE_PRIVATE))

class AndroidSettings constructor(private val delegate: SharedPreferences): Settings {

    override fun getStringOrNull(key: String): String? = delegate.getString(key, null)

    override fun putString(key: String, value: String?) {
        delegate.edit().putString(key, value).apply()
    }

    override fun getIntOrDefault(key: String, default: Int): Int = delegate.getInt(key, default)

    override fun putInt(key: String, value: Int) {
        delegate.edit().putInt(key, value).apply()
    }

    override fun getBooleanOrDefault(key: String, default: Boolean): Boolean = delegate.getBoolean(key, default)

    override fun putBoolean(key: String, value: Boolean) {
        delegate.edit().putBoolean(key, value).apply()
    }
}