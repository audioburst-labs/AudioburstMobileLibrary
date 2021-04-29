package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.BurstsResponse
import com.audioburst.library.data.repository.models.CtaDataResponse
import com.audioburst.library.models.Burst
import com.audioburst.library.models.CtaData
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration
import com.audioburst.library.utils.LibraryConfiguration
import io.ktor.http.*

internal class BurstResponseToBurstMapper constructor(
    private val libraryConfiguration: LibraryConfiguration,
    private val sourceResponseToBurstSourceMapper: SourceResponseToBurstSourceMapper,
) {

    fun map(from: BurstsResponse, userId: String, queryId: Long): Burst =
        Burst(
            id = from.burstId,
            title = from.title,
            creationDate = from.creationDateISO,
            sourceName = from.source.sourceName,
            duration = from.duration.toDuration(DurationUnit.Seconds),
            showName = from.source.showName,
            streamUrl = from.contentURLs.streamURL,
            audioUrl = from.contentURLs.audioURL,
            imageUrls = from.contentURLs.imageURL.firstOrNull()?.let { listOf(it) } ?: emptyList(),
            playlistId = queryId,
            category = from.category,
            source = sourceResponseToBurstSourceMapper.map(from.source),
            shareUrl = from.shareUrl(),
            keywords = from.entities ?: emptyList(),
            ctaData = from.promote?.ctaData?.toCtaData(),
            adUrl = from.adUrl(userId)
        )

    private fun BurstsResponse.shareUrl(): String =
        URLBuilder(contentURLs.searchSiteURL).apply {
            parameters.append(UTM_SOURCE_QUERY_NAME, libraryConfiguration.libraryKey.value)
        }.buildString()

    private fun BurstsResponse.adUrl(userId: String): String? =
        promote?.url?.let { adUrl ->
            try {
                URLBuilder(adUrl).apply {
                    if (!adUrl.contains(USER_ID_QUERY_PARAM)) {
                        parameters.append(USER_ID_QUERY_PARAM, userId)
                    }
                    category?.let { parameters.append(CATEGORY_QUERY_PARAM, it) }
                    parameters.append(AD_ONLY_QUERY_PARAM, true.toString())
                }.buildString()
            } catch (exception: URLParserException) {
                null
            }
        }

    private fun CtaDataResponse.toCtaData(): CtaData =
        CtaData(
            buttonText = ButtonText,
            url = URL,
        )

    companion object {
        private const val UTM_SOURCE_QUERY_NAME = "utm_source"
        private const val USER_ID_QUERY_PARAM = "userId"
        private const val CATEGORY_QUERY_PARAM = "category"
        private const val AD_ONLY_QUERY_PARAM = "adonly"
    }
}
