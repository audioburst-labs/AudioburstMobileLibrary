package com.audioburst.library.utils

import com.audioburst.library.models.PlayerSessionId

internal interface PlayerSessionIdGetter {

    fun get(): PlayerSessionId
}

internal class UuidBasedPlayerSessionIdGetter (
    private val uuidFactory: UuidFactory
): PlayerSessionIdGetter {

    override fun get(): PlayerSessionId = PlayerSessionId(uuidFactory.getUuid())
}

internal fun interface UuidFactory {

    fun getUuid(): String
}

internal expect class PlatformUuidFactory() : UuidFactory