package com.audioburst.library.models

internal data class PlayerEvent(
    val userId: String,
    val burstId: String?,
    val playerVersion: String,
    val subscriptionKey: SubscriptionKey,
    val time: Long,
    val playlistName: String?,
    val header: String,
    val appSessionId: String,
    val burstLength: Double?,
    val playerInstanceId: String,
    val playerStatus: Status,
    val playlistQueryId: Long,
    val playlistId: String?,
    val positionInBurst: Double?,
    val stream: Boolean?,
    val totalPlayTime: Double?,
    val pageViewId: String,
    val advertisementEvent: AdvertisementEvent?,
) {

    enum class Status {
        Playing, Stopped
    }
}
