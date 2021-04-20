package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.LibraryKey
import com.audioburst.library.models.LibraryVersion
import com.audioburst.library.models.SessionId
import com.audioburst.library.models.SubscriptionKey
import com.audioburst.library.utils.LibraryConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BurstResponseToBurstTest {

    private val libraryKey = "libraryKey"
    private val userId = "userId"

    private val mapper = BurstResponseToBurstMapper(
        libraryConfiguration = libraryConfigurationOf(libraryKey = libraryKey),
        sourceResponseToBurstSourceMapper = SourceResponseToBurstSourceMapper(),
    )

    @Test
    fun testMapper() {
        // GIVEN
        val source = sourceResponseOf()
        val response = burstsResponseOf(
            source = source
        )
        val queryId = 0L

        // WHEN
        val mapped = mapper.map(response, userId, queryId)

        // THEN
        assertEquals(mapped.id, response.burstId)
        assertEquals(mapped.title, response.title)
        assertEquals(mapped.creationDate, response.creationDate)
        assertEquals(mapped.sourceName, response.source.sourceName)
        assertEquals(mapped.category, response.category)
        assertEquals(mapped.duration.seconds, response.duration)
        assertEquals(mapped.playlistId, queryId)
        assertEquals(mapped.showName, response.source.showName)
        assertEquals(mapped.streamUrl, response.contentURLs.streamURL)
        assertEquals(mapped.audioUrl, response.contentURLs.audioURL)
        assertEquals(mapped.ctaData?.url, response.promote?.ctaData?.URL)
        assertEquals(mapped.ctaData?.buttonText, response.promote?.ctaData?.ButtonText)
    }

    @Test
    fun testIfKeywordsAreProperlyParsed() {
        // GIVEN
        val nullKeywordsResponse = burstsResponseOf(entities = null)
        val keywords = listOf("")
        val notNullKeywordsResponse = burstsResponseOf(entities = keywords)

        // WHEN
        val mappedNullKeywordsResponse = mapper.map(nullKeywordsResponse, userId, queryId = 0L)
        val mappedNotNullKeywordsResponse = mapper.map(notNullKeywordsResponse, userId, queryId = 0L)

        // THEN
        assertTrue(mappedNullKeywordsResponse.keywords.isEmpty())
        assertEquals(mappedNotNullKeywordsResponse.keywords.size, keywords.size)
    }

    @Test
    fun testIfShareUrlIsBeingBuildCorrectly() {
        // GIVEN
        val response = burstsResponseOf(
            contentURLs = contentURLsResponseOf(
                searchSiteURL = "https://search.audioburst.com/burst/oN9Nv4y940X6/Lawmakers-lay-out-arguments-for-against-Trump%27s-Supreme-Court-nominee?ref=AndroidApp&pid=1&v=1"
            )
        )

        // WHEN
        val mapped = mapper.map(response, userId, queryId = 0L)

        // THEN
        assertTrue(mapped.shareUrl.contains("utm_source"))
        assertTrue(mapped.shareUrl.contains(libraryKey))
    }

    @Test
    fun testIfImageUrlsAreProperlyParsed() {
        // GIVEN
        val emptyImageUrlListResponse = burstsResponseOf(
            contentURLs = contentURLsResponseOf(
                imageURL = emptyList()
            )
        )
        val firstImageUrl = "image.com"
        val notEmptyImageUrlListResponse = burstsResponseOf(
            contentURLs = contentURLsResponseOf(
                imageURL = listOf(firstImageUrl)
            )
        )

        // WHEN
        val mappedEmptyImageUrlListResponse = mapper.map(emptyImageUrlListResponse, userId, queryId = 0L)
        val mappedNotEmptyImageUrlListResponse = mapper.map(notEmptyImageUrlListResponse, userId, queryId = 0L)

        // THEN
        assertTrue(mappedEmptyImageUrlListResponse.imageUrls.isEmpty())
        assertTrue(mappedNotEmptyImageUrlListResponse.imageUrls.isNotEmpty())
        assertEquals(mappedNotEmptyImageUrlListResponse.imageUrls.first(), firstImageUrl)
    }

    @Test
    fun testIfAdUrlIsGettingParsedProperlyWhenPromoteResponseIsNull() {
        // GIVEN
        val nullPromoteResponse = burstsResponseOf(promote = null)

        // WHEN
        val mappedNullPromoteResponse = mapper.map(nullPromoteResponse, userId, queryId = 0L)

        // THEN
        assertTrue(mappedNullPromoteResponse.adUrl == null)
        assertTrue(!mappedNullPromoteResponse.isAdAvailable)
    }

    @Test
    fun testIfAdUrlIsGettingParsedProperlyWhenPromoteResponseIsNotNull() {
        // GIVEN
        val burstId = "burstId"
        val notNullPromoteResponse = burstsResponseOf(burstId = burstId, promote = promoteResponseOf())
        val expectedUrl = "https://sapi.audioburst.com/audio/get/streamwithad/$burstId?userId=$userId"

        // WHEN
        val mappedNotNullKeywordsResponse = mapper.map(notNullPromoteResponse, userId, queryId = 0L)

        // THEN
        assertEquals(expectedUrl, mappedNotNullKeywordsResponse.adUrl)
    }

    @Test
    fun testIfAdUrlIsGettingParsedProperlyWhenPromoteResponseIsNotNullAndCategoryIsAvailable() {
        // GIVEN
        val burstId = "burstId"
        val category = "category"
        val notNullPromoteResponse = burstsResponseOf(
                burstId = burstId,
                category = category,
                promote = promoteResponseOf()
        )
        val expectedUrl = "https://sapi.audioburst.com/audio/get/streamwithad/$burstId?userId=$userId&category=$category"

        // WHEN
        val mappedNotNullKeywordsResponse = mapper.map(notNullPromoteResponse, userId, queryId = 0L)

        // THEN
        assertEquals(expectedUrl, mappedNotNullKeywordsResponse.adUrl)
    }
}

internal fun libraryConfigurationOf(
    sessionId: String = "",
    libraryKey: String = "",
    libraryVersion: String = "",
    subscriptionKey: String = "",
): LibraryConfiguration =
    object : LibraryConfiguration {
        override val sessionId: SessionId = SessionId(sessionId)
        override val libraryKey: LibraryKey = LibraryKey(libraryKey)
        override val libraryVersion: LibraryVersion = LibraryVersion(libraryVersion)
        override val subscriptionKey: SubscriptionKey = SubscriptionKey(subscriptionKey)
    }
