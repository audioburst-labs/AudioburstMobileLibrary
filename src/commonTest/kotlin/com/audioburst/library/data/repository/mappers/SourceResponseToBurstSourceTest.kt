package com.audioburst.library.data.repository.mappers

import kotlin.test.Test
import kotlin.test.assertEquals

class SourceResponseToBurstSourceTest {

    private val mapper = SourceResponseToBurstSourceMapper()

    @Test
    fun map() {
        // GIVEN
        val response = sourceResponseOf()

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(mapped.sourceName, response.sourceName)
        assertEquals(mapped.sourceType, response.sourceType)
        assertEquals(mapped.showName, response.showName)
        assertEquals(mapped.durationFromStart.seconds, response.position)
        assertEquals(mapped.audioUrl, response.audioURL)
    }
}
