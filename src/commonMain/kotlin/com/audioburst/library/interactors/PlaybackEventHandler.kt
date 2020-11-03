package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.onData
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.AdvertisementEvent
import com.audioburst.library.models.EventPayload
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.PlayerEvent
import com.audioburst.library.utils.LibraryConfiguration

internal interface PlaybackEventHandler {
    suspend fun handle(playbackEvent: PlaybackEvent)
}

internal class PlaybackEventHandlerInteractor(
    private val userStorage: UserStorage,
    private val userRepository: UserRepository,
    private val unsentEventStorage: UnsentEventStorage,
    private val libraryConfiguration: LibraryConfiguration,
) : PlaybackEventHandler {

    override suspend fun handle(playbackEvent: PlaybackEvent) {
        when (playbackEvent) {
            is PlaybackEvent.AdListened -> postAdvertisementEvent(playbackEvent)
            else -> postPlayerEvent(playbackEvent)
        }
    }

    private suspend fun postAdvertisementEvent(adListened: PlaybackEvent.AdListened) {
        userRepository.postReportingData(adListened.reportingData) onData {
            val event = adListened.toEvent()
            val resource = userRepository.postEvent(adListened.toEvent())
            if (resource is Resource.Error) {
                unsentEventStorage.add(event)
            }
        }
    }

    private fun PlaybackEvent.AdListened.toEvent(): AdvertisementEvent =
        AdvertisementEvent(
            id = advertisement.id,
            type = advertisement.type,
            pixelURL = advertisement.pixelURL,
            duration = advertisement.duration.milliseconds.toString(),
            audioURL = advertisement.audioURL,
            provider = advertisement.provider,
            position = advertisement.position,
            positionText = reportingData.text,
            currentPosition = reportingData.position,
            currentPixelURL = reportingData.url
        )

    private suspend fun postPlayerEvent(playbackEvent: PlaybackEvent) {
        val userId = userStorage.userId ?: return
        val playerEvent = playbackEvent.eventPayload.toPlayerEvent(userId)
        val resource = userRepository.postEvent(playerEvent, playbackEvent.actionName)
        if (resource is Resource.Error) {
            unsentEventStorage.add(playerEvent)
        }
        if (playbackEvent is PlaybackEvent.StartOfPlay) {
            userRepository.postBurstPlayback(
                playlistId = playbackEvent.eventPayload.playlistId.toLong(),
                burstId = playbackEvent.eventPayload.burst.id,
                userId = userId,
            )
        }
    }

    private fun EventPayload.toPlayerEvent(userId: String): PlayerEvent =
        PlayerEvent(
            burstLength = burst.duration.seconds,
            playerInstanceId = playerSessionId.value,
            playerStatus = playerStatus(isPlaying),
            playlistQueryId = burst.playlistId,
            positionInBurst = currentPlayBackPosition.seconds,
            stream = burst.streamUrl != null,
            totalPlayTime = currentPlayBackPosition.seconds,
            burstId = burst.id,
            playlistId = playlistId.toString(),
            userId = userId,
            playerVersion = libraryConfiguration.libraryVersion.value,
            subscriptionKey = libraryConfiguration.subscriptionKey,
            time = occurrenceTime,
            playlistName = playlistName,
            header = libraryConfiguration.libraryKey.value,
            appSessionId = libraryConfiguration.sessionId.value,
            pageViewId = playerSessionId.value,
        )

    private fun playerStatus(isPlaying: Boolean): PlayerEvent.Status =
        if (isPlaying) PlayerEvent.Status.Playing else PlayerEvent.Status.Stopped
}
