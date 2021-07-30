package com.audioburst.library.data.repository.models

import kotlinx.serialization.Serializable

@Serializable
internal data class EventRequest(
    val audio_state: String,
    val burst_id: String,
    val audioburst_appkey: String,
    val cookie_uid: String,
    val player_version: String,
    val client_ts: Long,
    val events_source: String,
    val playlist_name: String,
    val playlist_id: String,
    val player_instance_id: String,
    val query_id: Long,
    val pageview_id: String,
    val app_session_id: String,
    val ad: String,
    val action_type: String,
    val action_value: String,
    val player_type: String,
    val screen_size: String,
    val experience_id: String,
    val player_settings: String,
    val page_url: String,
    val referrer_url: String,
    val cta_title: String,
    val cta_link: String,
    val SDK_level: String?,
    val SDK_version: String?,
)

@Serializable
internal data class AudioStateRequest(
    val burst_id: String?,
    val burst_length: Double?,
    val player_status: String,
    val position_in_burst: Double?,
    val stream: Boolean?,
    val total_play_time: Double?
)
