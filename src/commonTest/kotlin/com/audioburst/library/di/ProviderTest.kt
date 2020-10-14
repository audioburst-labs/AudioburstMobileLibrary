package com.audioburst.library.di

import com.audioburst.library.di.providers.Provider
import com.audioburst.library.di.providers.provider
import com.audioburst.library.di.providers.singleton
import kotlin.test.Test
import kotlin.test.assertEquals

class ProviderTest {

    @Test
    fun testNewInstanceProvider() {
        // GIVEN
        val provider: Provider<Mock> = provider { Mock() }

        // WHEN
        val mocks = Array(100) { provider.get() }

        // THEN
        assertEquals(mocks.toSet().size, mocks.size)
    }

    @Test
    fun testSingleInstanceProvider() {
        // GIVEN
        val provider: Provider<Mock> = singleton { Mock() }

        // WHEN
        val mocks = Array(100) { provider.get() }

        // THEN
        assertEquals(mocks.toSet().size, 1)
    }
}

private class Mock