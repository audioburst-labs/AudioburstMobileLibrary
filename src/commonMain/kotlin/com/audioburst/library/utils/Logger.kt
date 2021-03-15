package com.audioburst.library.utils

internal expect object PlatformLogger {

    fun log(severity: Severity, tag: String, message: String)
}

internal object Logger {

    private const val TAG = "AudioburstMobileLibrary"

    fun v(message: String, tag: String = TAG) {
        PlatformLogger.log(Severity.Verbose, tag, message)
    }

    fun d(message: String, tag: String = TAG) {
        PlatformLogger.log(Severity.Debug, tag, message)
    }

    fun i(message: String, tag: String = TAG) {
        PlatformLogger.log(Severity.Info, tag, message)
    }

    fun w(message: String, tag: String = TAG) {
        PlatformLogger.log(Severity.Warn, tag, message)
    }

    fun e(message: String, tag: String = TAG) {
        PlatformLogger.log(Severity.Error, tag, message)
    }
}

internal enum class Severity(val shorthand: String) {
    Verbose(shorthand = "v"),
    Debug(shorthand = "d"),
    Info(shorthand = "i"),
    Warn(shorthand = "w"),
    Error(shorthand = "e"),
}