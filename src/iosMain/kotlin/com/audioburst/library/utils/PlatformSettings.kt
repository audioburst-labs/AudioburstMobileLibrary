package com.audioburst.library.utils

import platform.Foundation.NSUserDefaults

actual fun createSettings(name: String): Settings = AppleSettings(NSUserDefaults(suiteName = name))

class AppleSettings constructor(private val delegate: NSUserDefaults): Settings {

    override fun getStringOrNull(key: String): String? = delegate.stringForKey(key)

    override fun putString(key: String, value: String?) {
        delegate.setObject(value, key)
    }
}