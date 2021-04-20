package com.audioburst.library.models

import com.audioburst.library.utils.LibraryConfiguration

internal data class PlayerEvent(
    val userId: String,
    val burstId: String? = null,
    val libraryConfiguration: LibraryConfiguration,
    val time: Long,
    val playlistName: String? = null,
    val burstLength: Double? = null,
    val playerInstanceId: String? = null,
    val playerStatus: Status? = null,
    val playlistQueryId: Long? = null,
    val playlistId: String? = null,
    val positionInBurst: Double? = null,
    val stream: Boolean? = null,
    val totalPlayTime: Double? = null,
    val pageViewId: String? = null,
    val advertisementEvent: AdvertisementEvent? = null,
    val action: PlayerAction? = null,
    val ctaButtonText: String? = null,
    val ctaUrl: String? = null,
) {

    enum class Status {
        Playing, Stopped
    }
}
