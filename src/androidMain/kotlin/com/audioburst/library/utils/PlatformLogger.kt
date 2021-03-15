package com.audioburst.library.utils

import android.util.Log

internal actual object PlatformLogger {

    actual fun log(severity: Severity, tag: String, message: String) {
        when (severity) {
            Severity.Verbose -> Log.v(tag, message)
            Severity.Debug -> Log.d(tag, message)
            Severity.Info -> Log.i(tag, message)
            Severity.Warn -> Log.w(tag, message)
            Severity.Error -> Log.e(tag, message)
        }
    }
}