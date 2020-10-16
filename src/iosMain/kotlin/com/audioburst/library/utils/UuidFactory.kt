package com.audioburst.library.utils

import platform.Foundation.NSUUID

actual object UuidFactory {

    actual fun getUuid(): String = NSUUID().UUIDString()
}