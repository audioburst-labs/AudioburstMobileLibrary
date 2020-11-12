package com.audioburst.library.data.repository.mappers

import kotlin.test.Test
import kotlin.test.assertEquals

class PromoteResponseToAdvertisementTest {

    private val mapper = AdvertisementResponseToAdvertisementMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val promote = advertisementResponseOf()

        // WHEN
        val mapped = mapper.map(promote)

        // THEN
        assertEquals(mapped.id, promote.adData.id)
        assertEquals(mapped.type, promote.type)
        assertEquals(mapped.audioURL, promote.adData.audioURL)
        assertEquals(mapped.duration.seconds, promote.adData.duration)
        assertEquals(mapped.pixelURL, promote.adData.pixelURL)
        assertEquals(mapped.position, promote.adData.position)
        assertEquals(mapped.provider, promote.adData.provider)
        assertEquals(mapped.reportingData.size, promote.adData.reportingPixelURLs.size)
    }
}
