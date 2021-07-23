package com.audioburst.library.data.repository.cache

import co.touchlab.stately.collections.IsoMutableMap
import com.audioburst.library.models.BurstShareUrl

internal class BurstShareUrlCache : Cache<String, BurstShareUrl> {

    private val map = IsoMutableMap<String, BurstShareUrl>()

    override suspend fun get(key: String): BurstShareUrl? = map[key]

    override suspend fun set(key: String, value: BurstShareUrl) {
        map[key] = value
    }
}