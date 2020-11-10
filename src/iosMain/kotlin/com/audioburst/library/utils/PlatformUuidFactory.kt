package com.audioburst.library.utils

import platform.Foundation.NSUUID

internal actual class PlatformUuidFactory : UuidFactory {

    override fun getUuid(): String = NSUUID().UUIDString()
}
