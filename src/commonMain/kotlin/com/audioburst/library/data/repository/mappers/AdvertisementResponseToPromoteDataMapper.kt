package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AdvertisementReportingPixelURLResponse
import com.audioburst.library.data.repository.models.AdvertisementResponse
import com.audioburst.library.models.*

internal class AdvertisementResponseToPromoteDataMapper {

    fun map(from: AdvertisementResponse): PromoteData =
        PromoteData(
            advertisement = from.adData?.let { adData ->
                Advertisement(
                    burstUrl = from.url,
                    type = from.type,
                    id = adData.id,
                    audioURL = adData.audioURL,
                    duration = adData.duration.toDuration(DurationUnit.Seconds),
                    pixelURL = adData.pixelURL,
                    position = adData.position,
                    provider = adData.provider,
                    reportingData = adData.reportingPixelURLs.map { it.toReportingData() }
                )
            },
            ctaData = null,
        )

    private fun AdvertisementReportingPixelURLResponse.toReportingData(): ReportingData =
        ReportingData(
            url = pixelURL,
            text = positionText,
            position = position
        )
}
