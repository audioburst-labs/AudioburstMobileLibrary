package com.audioburst.library.utils

import java.util.*

internal actual object UuidFactory {

    actual fun getUuid(): String = UUID.randomUUID().toString()
}
