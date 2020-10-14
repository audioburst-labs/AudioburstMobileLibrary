package com.audioburst.library.data.repository.mappers

import kotlin.test.Test
import kotlin.test.assertEquals

class PromoteResponseToAdvertisementTest {

    private val mapper = PromoteResponseToAdvertisementMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val promote = promoteResponseOf()

        // WHEN
        val mapped = mapper.map(promote)

        // THEN
        assertEquals(mapped.id, promote.adData.Id)
        assertEquals(mapped.type, promote.type)
        assertEquals(mapped.audioURL, promote.adData.AudioURL)
        assertEquals(mapped.duration.seconds, promote.adData.Duration)
        assertEquals(mapped.pixelURL, promote.adData.PixelURL)
        assertEquals(mapped.position, promote.adData.Position)
        assertEquals(mapped.provider, promote.adData.Provider)
        assertEquals(mapped.reportingData.size, promote.adData.ReportingPixelURLs.size)
    }
}
