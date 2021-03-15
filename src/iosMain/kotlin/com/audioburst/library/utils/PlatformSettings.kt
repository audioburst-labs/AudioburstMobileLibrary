package com.audioburst.library.utils

import platform.Foundation.NSUserDefaults

actual fun createSettings(name: String): Settings = AppleSettings(NSUserDefaults(suiteName = name))

class AppleSettings constructor(private val delegate: NSUserDefaults): Settings {

    private fun hasKey(key: String): Boolean = delegate.objectForKey(key) != null

    override fun getStringOrNull(key: String): String? = delegate.stringForKey(key)

    override fun putString(key: String, value: String?) {
        delegate.setObject(value, key)
    }

    override fun getIntOrDefault(key: String, default: Int): Int =
        if (hasKey(key)) delegate.integerForKey(key).toInt() else default

    override fun putInt(key: String, value: Int) {
        delegate.setInteger(value.toLong(), key)
    }
}