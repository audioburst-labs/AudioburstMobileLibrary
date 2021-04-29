package com.audioburst.library.data.repository.mappers

import kotlin.test.Test
import kotlin.test.assertEquals

class PromoteResponseToAdvertisementTest {

    private val mapper = AdvertisementResponseToPromoteDataMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val promote = advertisementResponseOf(
            adData = advertisementAdDataResponseOf()
        )

        // WHEN
        val mapped = mapper.map(promote)

        // THEN
        assertEquals(mapped.advertisement?.id, promote.adData?.id)
        assertEquals(mapped.advertisement?.type, promote.type)
        assertEquals(mapped.advertisement?.audioURL, promote.adData?.audioURL)
        assertEquals(mapped.advertisement?.duration?.seconds, promote.adData?.duration)
        assertEquals(mapped.advertisement?.pixelURL, promote.adData?.pixelURL)
        assertEquals(mapped.advertisement?.position, promote.adData?.position)
        assertEquals(mapped.advertisement?.provider, promote.adData?.provider)
        assertEquals(mapped.advertisement?.reportingData?.size, promote.adData?.reportingPixelURLs?.size)
    }

    @Test
    fun testMapperWhenAdDataIsNull() {
        // GIVEN
        val promote = advertisementResponseOf(adData = null)

        // WHEN
        val mapped = mapper.map(promote)

        // THEN
        assertEquals(null, mapped.advertisement)
    }
}
