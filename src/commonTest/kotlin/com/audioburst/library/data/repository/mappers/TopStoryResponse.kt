package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.*

internal fun topStoryResponseOf(
    type: String? = null,
    queryID: Long = 0L,
    query: String? = null,
    actualQuery: String? = null,
    bursts: List<BurstsResponse>? = null,
    message: String? = null,
    imageURL: String? = null,
    nextPage: String? = null,
    intent: String? = null,
): TopStoryResponse =
    TopStoryResponse(
        type = type,
        queryID = queryID,
        query = query,
        actualQuery = actualQuery,
        bursts = bursts,
        message = message,
        imageURL = imageURL,
        nextPage = nextPage,
        intent = intent,
    )

internal fun burstsResponseOf(
    index: Int = 0,
    burstId: String = "",
    creationDate: String = "",
    creationDateISO: String = "",
    title: String = "",
    quote: String? = null,
    score: Double = 0.0,
    text: String? = null,
    duration: Double = 0.0,
    category: String? = null,
    keywords: List<String>? = null,
    entities: List<String>? = null,
    source: SourceResponse = sourceResponseOf(),
    contentURLs: ContentURLsResponse = contentURLsResponseOf(),
    playlistScore: Int = 0,
    publicationDate: String = "",
    publicationDateISO: String = "",
    transcript: String? = null,
    audioData: AudioData? = null,
    promote: PromoteResponse? = null,
): BurstsResponse =
    BurstsResponse(
        index = index,
        burstId = burstId,
        creationDate = creationDate,
        creationDateISO = creationDateISO,
        title = title,
        quote = quote,
        score = score,
        text = text,
        duration = duration,
        category = category,
        keywords = keywords,
        entities = entities,
        source = source,
        contentURLs = contentURLs,
        playlistScore = playlistScore,
        publicationDate = publicationDate,
        publicationDateISO = publicationDateISO,
        transcript = transcript,
        audioData = audioData,
        promote = promote,
    )

internal fun sourceResponseOf(
    sourceId: Int = 0,
    sourceName: String = "",
    sourceType: String? = null,
    showId: Int = 0,
    showName: String = "",
    position: Double = 0.0,
    audioURL: String? = null,
    imageURL: String = "",
    titleSource: String? = null,
    location: LocationResponse? = null,
    episodeName: String? = null,
    network: List<String>? = null,
): SourceResponse =
    SourceResponse(
        sourceId = sourceId,
        sourceName = sourceName,
        sourceType = sourceType,
        showId = showId,
        showName = showName,
        position = position,
        audioURL = audioURL,
        imageURL = imageURL,
        titleSource = titleSource,
        location = location,
        episodeName = episodeName,
        network = network,
    )

internal fun contentURLsResponseOf(
    streamURL: String? = null,
    burstURL: String = "",
    audioURL: String = "",
    imageURL: List<String> = emptyList(),
    searchSiteURL: String = "www.google.com",
): ContentURLsResponse =
    ContentURLsResponse(
        streamURL = streamURL,
        burstURL = burstURL,
        audioURL = audioURL,
        imageURL = imageURL,
        searchSiteURL = searchSiteURL,
    )

internal fun promoteResponseOf(
    adData: AdDataResponse? = null,
    type: String? = null,
    url: String? = null,
    ctaDataResponse: CtaDataResponse? = null,
): PromoteResponse =
    PromoteResponse(
        adData = adData,
        type = type,
        url = url,
        ctaData = ctaDataResponse,
    )

internal fun ctaDataResponseOf(
    ButtonText: String = "",
    URL: String = "",
    OpenInNewTab: Boolean = false,
): CtaDataResponse =
    CtaDataResponse(
        ButtonText = ButtonText,
        URL = URL,
        OpenInNewTab = OpenInNewTab,
    )

internal fun adDataResponseOf(
    AudioURL: String = "",
    Duration: Double = 0.0,
    Id: String = "",
    PixelURL: String = "",
    Position: String = "",
    Provider: String = "",
    ReportingPixelURLs: List<ReportingPixelURLResponse> = emptyList(),
): AdDataResponse =
    AdDataResponse(
        AudioURL = AudioURL,
        Duration = Duration,
        Id = Id,
        PixelURL = PixelURL,
        Position = Position,
        Provider = Provider,
        ReportingPixelURLs = ReportingPixelURLs,
    )

internal fun reportingPixelURLResponseOf(
    PixelURL: String = "",
    PositionText: String = "",
    Position: Double = 0.0,
): ReportingPixelURLResponse =
    ReportingPixelURLResponse(
        PixelURL = PixelURL,
        PositionText = PositionText,
        Position = Position,
    )

internal fun advertisementResponseOf(
    adData: AdvertisementDataResponse? = null,
    type: String = "",
    url: String? = null,
): AdvertisementResponse =
    AdvertisementResponse(
        adData = adData,
        type = type,
        url = url,
    )

internal fun advertisementAdDataResponseOf(
    audioURL: String = "",
    duration: Double = 0.0,
    id: String = "",
    pixelURL: String = "",
    position: String = "",
    provider: String = "",
    reportingPixelURLs: List<AdvertisementReportingPixelURLResponse> = emptyList(),
): AdvertisementDataResponse =
    AdvertisementDataResponse(
        audioURL = audioURL,
        duration = duration,
        id = id,
        pixelURL = pixelURL,
        position = position,
        provider = provider,
        reportingPixelURLs = reportingPixelURLs,
    )
