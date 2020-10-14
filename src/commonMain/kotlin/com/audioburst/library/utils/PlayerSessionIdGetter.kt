package com.audioburst.library.utils

import com.audioburst.library.models.PlayerSessionId

internal interface PlayerSessionIdGetter {

    fun get(): PlayerSessionId
}

internal class UuidBasedPlayerSessionIdGetter : PlayerSessionIdGetter {

    // TODO: Implement it as a UUID
    override fun get(): PlayerSessionId =
        PlayerSessionId("")
}
