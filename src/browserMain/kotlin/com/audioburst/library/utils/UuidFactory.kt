package com.audioburst.library.utils

internal actual object UuidFactory {
    actual fun getUuid(): String = uuidv4()
}
