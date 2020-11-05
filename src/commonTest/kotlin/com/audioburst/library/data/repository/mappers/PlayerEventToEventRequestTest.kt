package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.AdvertisementEvent
import com.audioburst.library.models.PlayerEvent
import com.audioburst.library.models.SubscriptionKey
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerEventToEventRequestTest {

    private val mapper = PlayerEventToEventRequestMapper(
        json = Json {  },
        advertisementEventToAdvertisementEventRequestMapper = AdvertisementEventToAdvertisementEventRequestMapper()
    )

    @Test
    fun testMapper() {
        // GIVEN
        val playerEvent = playerEventOf()

        // WHEN
        val mapped = mapper.map(playerEvent)

        // THEN
        assertEquals(mapped.burst_id, playerEvent.burstId)
        assertEquals(mapped.cookie_uid, playerEvent.userId)
        assertEquals(mapped.player_version, playerEvent.playerVersion)
        assertEquals(mapped.player_instance_id, playerEvent.playerInstanceId)
        assertEquals(mapped.query_id, playerEvent.playlistQueryId)
        assertEquals(mapped.client_ts, playerEvent.time)
        assertEquals(mapped.playlist_id, playerEvent.playlistId)
        assertEquals(mapped.events_source, playerEvent.header)
        assertEquals(mapped.playlist_name, playerEvent.playlistName)
        assertEquals(mapped.pageview_id, playerEvent.pageViewId)
        assertEquals(mapped.app_session_id, playerEvent.appSessionId)
    }
}

internal fun playerEventOf(
    userId: String = "",
    burstId: String? = null,
    playerVersion: String = "",
    time: Long = 0L,
    playlistName: String? = null,
    header: String = "",
    subscriptionKey: SubscriptionKey = SubscriptionKey(""),
    appSessionId: String = "",
    burstLength: Double? = null,
    playerInstanceId: String = "",
    playerStatus: PlayerEvent.Status = PlayerEvent.Status.Playing,
    playlistQueryId: Long = 0L,
    playlistId: String? = null,
    positionInBurst: Double? = null,
    stream: Boolean? = null,
    totalPlayTime: Double? = null,
    pageViewId: String = "",
    advertisementEvent: AdvertisementEvent? = null,
) : PlayerEvent =
    PlayerEvent(
        userId = userId,
        burstId = burstId,
        playerVersion = playerVersion,
        time = time,
        playlistName = playlistName,
        header = header,
        subscriptionKey = subscriptionKey,
        appSessionId = appSessionId,
        burstLength = burstLength,
        playerInstanceId = playerInstanceId,
        playerStatus = playerStatus,
        playlistQueryId = playlistQueryId,
        playlistId = playlistId,
        positionInBurst = positionInBurst,
        stream = stream,
        totalPlayTime = totalPlayTime,
        pageViewId = pageViewId,
        advertisementEvent = advertisementEvent,
    )
