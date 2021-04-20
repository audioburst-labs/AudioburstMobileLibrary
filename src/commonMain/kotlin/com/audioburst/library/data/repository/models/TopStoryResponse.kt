package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class TopStoryResponse(
    val type: String?,
    val queryID: Long,
    val query: String?,
    val actualQuery: String?,
    val bursts: List<BurstsResponse>?,
    val message: String?,
    val imageURL: String?,
    val nextPage: String?,
)

@Serializable
internal data class BurstsResponse(
    val index: Int,
    val burstId: String,
    val creationDate: String,
    val creationDateISO: String,
    val title: String,
    val quote: String?,
    val score: Double,
    val text: String?,
    val duration: Double,
    val category: String?,
    val keywords: List<String>?,
    val entities: List<String>?,
    val source: SourceResponse,
    val contentURLs: ContentURLsResponse,
    val playlistScore: Int,
    val publicationDate: String,
    val publicationDateISO: String,
    val transcript: String?,
    val audioData: AudioData?,
    val promote: PromoteResponse?,
)

@Serializable
internal data class SourceResponse(
    val sourceId: Int,
    val sourceName: String,
    val sourceType: String?,
    val showId: Int,
    val showName: String,
    val position: Double,
    val audioURL: String?,
    val imageURL: String,
    val titleSource: String?,
    val location: LocationResponse?,
    val episodeName: String?,
    val network: List<String>?,
)

@Serializable
internal data class LocationResponse(
    val type: String,
    val coordinates: List<Double>,
    val text: String?,
)

@Serializable
internal data class ContentURLsResponse(
    val streamURL: String?,
    val burstURL: String,
    val audioURL: String,
    val imageURL: List<String>,
    val searchSiteURL: String,
)

@Serializable
internal data class AudioData(
    val suggestedIn: Double,
    val suggestedOut: Double,
)

@Serializable
internal data class PromoteResponse(
    val adData: AdDataResponse?,
    val type: String?,
    val url: String?,
    val ctaData: CtaDataResponse?,
)

@Serializable
internal data class AdDataResponse(
    val AudioURL: String,
    val Duration: Double,
    val Id: String,
    val PixelURL: String,
    val Position: String,
    val Provider: String,
    val ReportingPixelURLs: List<ReportingPixelURLResponse>,
)

@Serializable
internal data class ReportingPixelURLResponse(
    val PixelURL: String,
    val PositionText: String,
    val Position: Double,
)

@Serializable
data class CtaDataResponse(
    val ButtonText: String,
    val URL: String,
    val OpenInNewTab: Boolean,
)
