package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AdvertisementEventRequest
import com.audioburst.library.models.AdvertisementEvent

internal class AdvertisementEventToAdvertisementEventRequestMapper {

    fun map(from: AdvertisementEvent) =
        AdvertisementEventRequest(
            id = from.id,
            type = from.type,
            pixelURL = from.pixelURL,
            duration = from.duration,
            audioURL = from.audioURL,
            provider = from.provider,
            position = from.position,
            positionText = from.positionText,
            currentPosition = from.currentPosition,
            currentPixelURL = from.currentPixelURL
        )
}
