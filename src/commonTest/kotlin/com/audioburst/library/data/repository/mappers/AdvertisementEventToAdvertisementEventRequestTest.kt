package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.AdvertisementEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class AdvertisementEventToAdvertisementEventRequestTest {

    private val mapper = AdvertisementEventToAdvertisementEventRequestMapper()

    @Test
    fun testMapper() {
        // GIVEN
        val event = advertisementEventOf()

        // WHEN
        val mapped = mapper.map(event)

        // THEN
        assertEquals(event.id, mapped.id)
        assertEquals(event.type, mapped.type)
        assertEquals(event.pixelURL, mapped.pixelURL)
        assertEquals(event.duration, mapped.duration)
        assertEquals(event.audioURL, mapped.audioURL)
        assertEquals(event.provider, mapped.provider)
        assertEquals(event.position, mapped.position)
        assertEquals(event.positionText, mapped.positionText)
        assertEquals(event.currentPosition, mapped.currentPosition)
        assertEquals(event.currentPixelURL, mapped.currentPixelURL)
    }
}

internal fun advertisementEventOf(
    id: String = "",
    type: String = "",
    pixelURL: String = "",
    duration: String = "",
    audioURL: String = "",
    provider: String = "",
    position: String = "",
    positionText: String = "",
    currentPosition: Double = 0.0,
    currentPixelURL: String = "",
) : AdvertisementEvent =
    AdvertisementEvent(
        id = id,
        type = type,
        pixelURL = pixelURL,
        duration = duration,
        audioURL = audioURL,
        provider = provider,
        position = position,
        positionText = positionText,
        currentPosition = currentPosition,
        currentPixelURL = currentPixelURL,
    )
