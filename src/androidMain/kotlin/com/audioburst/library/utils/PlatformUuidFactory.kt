package com.audioburst.library.utils

import java.util.*

internal actual class PlatformUuidFactory : UuidFactory {
    
    override fun getUuid(): String = UUID.randomUUID().toString()
}
