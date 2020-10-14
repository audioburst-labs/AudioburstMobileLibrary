package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.AudioStateRequest
import com.audioburst.library.data.repository.models.EventRequest
import com.audioburst.library.models.PlayerEvent
import com.audioburst.library.utils.JsonEncoder

internal class PlayerEventToEventRequestMapper(
    private val jsonEncoder: JsonEncoder,
) {

    fun map(from: PlayerEvent) =
        EventRequest(
            audio_state = jsonEncoder.encodeToString(
                AudioStateRequest(
                    burst_id = from.burstId,
                    burst_length = from.burstLength,
                    player_status = from.playerStatus.name.toLowerCase(),
                    position_in_burst = from.positionInBurst,
                    stream = from.stream,
                    total_play_time = from.totalPlayTime
                )
            ),
            burst_id = from.burstId,
            cookie_uid = from.userId,
            player_version = from.playerVersion,
            player_instance_id = from.playerInstanceId,
            query_id = from.playlistQueryId,
            client_ts = from.time,
            playlist_id = from.playlistId,
            events_source = from.header,
            playlist_name = from.playlistName,
            experience_id = from.experienceId,
            pageview_id = from.pageViewId,
            app_session_id = from.appSessionId
        )
}
