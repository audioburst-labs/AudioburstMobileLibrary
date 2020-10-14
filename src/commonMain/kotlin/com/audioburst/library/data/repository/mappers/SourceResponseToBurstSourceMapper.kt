package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.SourceResponse
import com.audioburst.library.models.BurstSource
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration

internal class SourceResponseToBurstSourceMapper {

    fun map(from: SourceResponse): BurstSource =
        BurstSource(
            sourceName = from.sourceName,
            sourceType = from.sourceType,
            showName = from.showName,
            durationFromStart = from.position.toDuration(DurationUnit.Seconds),
            audioUrl = from.audioURL,
        )
}
