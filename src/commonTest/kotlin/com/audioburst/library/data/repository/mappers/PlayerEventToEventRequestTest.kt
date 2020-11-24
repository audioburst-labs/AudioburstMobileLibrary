package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.AdvertisementEvent
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.PlayerEvent
import com.audioburst.library.utils.LibraryConfiguration
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
        assertEquals(mapped.player_version, playerEvent.libraryConfiguration.libraryVersion.value)
        assertEquals(mapped.player_instance_id, playerEvent.playerInstanceId)
        assertEquals(mapped.query_id, playerEvent.playlistQueryId)
        assertEquals(mapped.client_ts, playerEvent.time)
        assertEquals(mapped.playlist_id, "")
        assertEquals(mapped.events_source, playerEvent.libraryConfiguration.libraryKey.value)
        assertEquals(mapped.playlist_name, "")
        assertEquals(mapped.pageview_id, playerEvent.pageViewId)
        assertEquals(mapped.app_session_id, playerEvent.libraryConfiguration.sessionId.value)
        assertEquals(mapped.action_type, playerEvent.action.type.id)
        assertEquals(mapped.action_value, playerEvent.action.value)
        assertEquals(mapped.player_type, "")
        assertEquals(mapped.screen_size, "")
        assertEquals(mapped.experience_id, "")
        assertEquals(mapped.player_settings, "{}")
        assertEquals(mapped.page_url, "")
        assertEquals(mapped.referrer_url, "")
        assertEquals(mapped.ab_cta, "")
        assertEquals(mapped.ab_cta_link, "")
    }
}

internal fun playerActionOf(
    type: PlayerAction.Type = PlayerAction.Type.Personalized,
    value: String = ""
): PlayerAction =
    PlayerAction(
        type = type,
        value = value,
    )

internal fun playerEventOf(
    userId: String = "",
    burstId: String = "",
    time: Long = 0L,
    playlistName: String? = null,
    libraryConfiguration: LibraryConfiguration = libraryConfigurationOf(),
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
    action: PlayerAction = playerActionOf()
) : PlayerEvent =
    PlayerEvent(
        userId = userId,
        burstId = burstId,
        libraryConfiguration = libraryConfiguration,
        time = time,
        playlistName = playlistName,
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
        action = action,
    )
