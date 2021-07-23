package com.audioburst.library.data.repository.cache

import com.audioburst.library.data.repository.appSettingsOf
import com.audioburst.library.data.repository.shareTextsOf
import com.audioburst.library.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppSettingsCacheTest {

    private lateinit var cache: AppSettingsCache

    @BeforeTest
    fun initializeCache() {
        cache = AppSettingsCache()
    }

    @Test
    fun `test if cache is empty null is returned`() = runTest {
        // GIVEN
        val key = Unit

        // WHEN
        val cached = cache.get(key)

        // THEN
        assertEquals(null, cached)
    }

    @Test
    fun `test if there is value for a given key it's getting returned`() = runTest {
        // GIVEN
        val key = Unit
        val value = appSettingsOf()

        // WHEN
        cache.set(key, value)
        val cached = cache.get(key)

        // THEN
        assertEquals(value, cached)
    }

    @Test
    fun `test if value is getting overridden`() = runTest {
        // GIVEN
        val key = Unit
        val first = appSettingsOf()
        val second = appSettingsOf(shareTexts = shareTextsOf(burst = "burst"))

        // WHEN
        cache.set(key, first)
        cache.set(key, second)
        val cached = cache.get(key)

        // THEN
        assertEquals(second, cached)
    }
}