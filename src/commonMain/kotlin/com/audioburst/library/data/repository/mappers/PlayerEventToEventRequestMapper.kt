package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AudioStateRequest
import com.audioburst.library.data.repository.models.EventRequest
import com.audioburst.library.models.PlayerEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class PlayerEventToEventRequestMapper(
    private val json: Json,
    private val advertisementEventToAdvertisementEventRequestMapper: AdvertisementEventToAdvertisementEventRequestMapper,
) {

    fun map(from: PlayerEvent) =
        EventRequest(
            audio_state = json.encodeToString(
                AudioStateRequest(
                    burst_id = from.burstId,
                    burst_length = from.burstLength,
                    player_status = from.playerStatus?.name?.toLowerCase() ?: EMPTY_STRING,
                    position_in_burst = from.positionInBurst,
                    stream = from.stream,
                    total_play_time = from.totalPlayTime
                )
            ),
            burst_id = from.burstId ?: EMPTY_STRING,
            cookie_uid = from.userId,
            audioburst_appkey = from.libraryConfiguration.subscriptionKey.value,
            player_version = from.libraryConfiguration.libraryVersion.value,
            player_instance_id = from.playerInstanceId ?: EMPTY_STRING,
            query_id = from.playlistQueryId ?: 0,
            client_ts = from.time,
            playlist_id = from.playlistId ?: EMPTY_STRING,
            events_source = from.libraryConfiguration.libraryKey.value,
            playlist_name = from.playlistName ?: EMPTY_STRING,
            pageview_id = from.pageViewId ?: EMPTY_STRING,
            app_session_id = from.libraryConfiguration.sessionId.value,
            ad = from.advertisementEvent?.let(advertisementEventToAdvertisementEventRequestMapper::map)?.let { json.encodeToString(it) } ?: EMPTY_OBJECT,
            action_type = from.action?.type?.id ?: EMPTY_STRING,
            action_value = from.action?.value ?: EMPTY_STRING,
            player_type = EMPTY_STRING,
            screen_size = EMPTY_STRING,
            experience_id = EMPTY_STRING,
            player_settings = EMPTY_OBJECT,
            page_url = EMPTY_STRING,
            referrer_url = EMPTY_STRING,
            cta_title = from.ctaButtonText ?: EMPTY_STRING,
            cta_link = from.ctaUrl ?: EMPTY_STRING,
        )

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_OBJECT = "{}"
    }
}
