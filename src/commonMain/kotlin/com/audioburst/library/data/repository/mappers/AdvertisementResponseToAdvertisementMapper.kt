package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AdvertisementReportingPixelURLResponse
import com.audioburst.library.data.repository.models.AdvertisementResponse
import com.audioburst.library.models.Advertisement
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.ReportingData
import com.audioburst.library.models.toDuration

internal class AdvertisementResponseToAdvertisementMapper {

    fun map(from: AdvertisementResponse): Advertisement =
        Advertisement(
            burstUrl = from.url,
            type = from.type,
            id = from.adData.id,
            audioURL = from.adData.audioURL,
            duration = from.adData.duration.toDuration(DurationUnit.Seconds),
            pixelURL = from.adData.pixelURL,
            position = from.adData.position,
            provider = from.adData.provider,
            reportingData = from.adData.reportingPixelURLs.map { it.toReportingData() }
        )

    private fun AdvertisementReportingPixelURLResponse.toReportingData(): ReportingData =
        ReportingData(
            url = pixelURL,
            text = positionText,
            position = position
        )
}
