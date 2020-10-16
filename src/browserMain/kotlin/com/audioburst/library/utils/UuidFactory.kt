package com.audioburst.library.utils

actual object UuidFactory {
    actual fun getUuid(): String = uuidv4()
}
