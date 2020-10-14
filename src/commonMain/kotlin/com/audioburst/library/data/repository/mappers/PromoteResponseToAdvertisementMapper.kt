package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.PromoteResponse
import com.audioburst.library.data.repository.models.ReportingPixelURLResponse
import com.audioburst.library.models.Advertisement
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.ReportingData
import com.audioburst.library.models.toDuration

internal class PromoteResponseToAdvertisementMapper {

    fun map(from: PromoteResponse): Advertisement =
        Advertisement(
            type = from.type,
            id = from.adData.Id,
            audioURL = from.adData.AudioURL,
            duration = from.adData.Duration.toDuration(DurationUnit.Seconds),
            pixelURL = from.adData.PixelURL,
            position = from.adData.Position,
            provider = from.adData.Provider,
            reportingData = from.adData.ReportingPixelURLs.map { it.toReportingData() }
        )

    private fun ReportingPixelURLResponse.toReportingData(): ReportingData =
        ReportingData(
            url = PixelURL,
            text = PositionText,
            position = Position
        )
}
