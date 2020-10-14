package com.audioburst.library.data.repository.models

internal data class EventRequest(
    val audio_state: String,
    val burst_id: String?,
    val cookie_uid: String,
    val player_version: String,
    val client_ts: Long,
    val events_source: String,
    val playlist_name: String?,
    val playlist_id: String?,
    val player_instance_id: String,
    val query_id: Long,
    val experience_id: String?,
    val pageview_id: String?,
    val app_session_id: String
)

internal data class AudioStateRequest(
    val burst_id: String?,
    val burst_length: Double?,
    val player_status: String,
    val position_in_burst: Double?,
    val stream: Boolean?,
    val total_play_time: Double?
)
