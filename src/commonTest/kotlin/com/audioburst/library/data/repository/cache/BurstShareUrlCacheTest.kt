package com.audioburst.library.data.repository.cache

import com.audioburst.library.interactors.burstShareUrlOf
import com.audioburst.library.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BurstShareUrlCacheTest {

    private lateinit var cache: BurstShareUrlCache

    @BeforeTest
    fun initializeCache() {
        cache = BurstShareUrlCache()
    }

    @Test
    fun `test if cache is empty null is returned`() = runTest {
        // GIVEN
        val key = "key"

        // WHEN
        val cached = cache.get(key)

        // THEN
        assertEquals(null, cached)
    }

    @Test
    fun `test if there is value for a given key it's getting returned`() = runTest {
        // GIVEN
        val key = "key"
        val value = burstShareUrlOf()

        // WHEN
        cache.set(key, value)
        val cached = cache.get(key)

        // THEN
        assertEquals(value, cached)
    }

    @Test
    fun `test if value is getting overridden`() = runTest {
        // GIVEN
        val key = "key"
        val first = burstShareUrlOf()
        val second = burstShareUrlOf(shortUrl = "shortUrl")

        // WHEN
        cache.set(key, first)
        cache.set(key, second)
        val cached = cache.get(key)

        // THEN
        assertEquals(second, cached)
    }
}