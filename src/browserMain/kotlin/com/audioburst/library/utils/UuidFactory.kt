package com.audioburst.library.utils

internal actual class PlatformUuidFactory : UuidFactory {

    override fun getUuid(): String = uuidv4()
}
