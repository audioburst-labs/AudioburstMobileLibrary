package com.audioburst.library.utils

import platform.Foundation.NSLog

internal actual object PlatformLogger {

    actual fun log(severity: Severity, tag: String, message: String) {
        NSLog("${severity.shorthand}/$tag: $message")
    }
}