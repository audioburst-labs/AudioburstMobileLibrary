package com.audioburst.library.models

import com.audioburst.library.utils.LibraryConfiguration

internal data class PlayerEvent(
    val userId: String,
    val burstId: String,
    val libraryConfiguration: LibraryConfiguration,
    val time: Long,
    val playlistName: String?,
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
    val action: PlayerAction,
) {

    enum class Status {
        Playing, Stopped
    }
}
