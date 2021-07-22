package com.audioburst.library.interactors

import com.audioburst.library.data.onError
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.storage.ListenedBurstStorage
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration
import com.audioburst.library.utils.Logger
import com.audioburst.library.utils.TimestampProvider

internal interface PlaybackEventHandler {
    suspend fun handle(event: Event)
}

internal class PlaybackEventHandlerInteractor(
    private val userStorage: UserStorage,
    private val userRepository: UserRepository,
    private val unsentEventStorage: UnsentEventStorage,
    private val libraryConfiguration: LibraryConfiguration,
    private val listenedBurstStorage: ListenedBurstStorage,
    private val timestampProvider: TimestampProvider,
) : PlaybackEventHandler {

    override suspend fun handle(event: Event) {
        Logger.i("Event detected: ${event.actionName}${event.logMessage}")
        when (event) {
            is PlaybackEvent -> when (event) {
                is PlaybackEvent.BurstListened -> markBurstAsListened(event.eventPayload.burst)
                is PlaybackEvent.AdListened -> postAdvertisementEvent(event)
                else -> postPlayerEvent(event)
            }
            is GeneralEvent.GetPlaylists -> postGeneralEvent(event)
            is GeneralEvent.UiEvent -> postEvent(playerEvent = event.playerEvent, eventName = event.actionName)
        }
    }

    private suspend fun markBurstAsListened(burst: Burst) {
        listenedBurstStorage.addOrUpdate(
            ListenedBurst(
                id = burst.id,
                dateTime = DateTime.now(),
            )
        )
    }

    private suspend fun postAdvertisementEvent(adListened: PlaybackEvent.AdListened) {
        userRepository.postReportingData(adListened.reportingData)
        postPlayerEvent(
            playbackEvent = adListened,
            advertisementEvent = adListened.toEvent()
        )
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

    private suspend fun postGeneralEvent(generalEvent: GeneralEvent) {
        val userId = userStorage.userId ?: return
        postEvent(
            PlayerEvent(
                userId = userId,
                libraryConfiguration = libraryConfiguration,
                time = timestampProvider.timeSince1970().milliseconds.toLong(),
            ),
            generalEvent.actionName,
        )
    }

    private suspend fun postPlayerEvent(playbackEvent: PlaybackEvent, advertisementEvent: AdvertisementEvent? = null) {
        val userId = userStorage.userId ?: return
        val playerEvent = when (playbackEvent) {
            is PlaybackEvent.CtaClick -> playbackEvent.toPlayerEvent(
                userId = userId,
                advertisementEvent = advertisementEvent,
                ctaButtonText = playbackEvent.buttonText,
                ctaUrl = playbackEvent.url,
            )
            else -> playbackEvent.toPlayerEvent(
                userId = userId,
                advertisementEvent = advertisementEvent,
            )
        }
        postEvent(playerEvent, playbackEvent.actionName)
        if (playbackEvent is PlaybackEvent.StartOfPlay) {
            userRepository.postBurstPlayback(
                playlistId = playbackEvent.eventPayload.burst.playlistId,
                burstId = playbackEvent.eventPayload.burst.id,
                userId = userId,
            )
        }
    }

    private suspend fun postEvent(playerEvent: PlayerEvent, eventName: String) {
        userRepository.postEvent(playerEvent, eventName).onError {
            unsentEventStorage.add(playerEvent)
        }
    }

    private fun PlaybackEvent.toPlayerEvent(
        userId: String,
        advertisementEvent: AdvertisementEvent? = null,
        ctaButtonText: String? = null,
        ctaUrl: String? = null,
    ): PlayerEvent = with(eventPayload) {
        PlayerEvent(
            burstLength = length(),
            playerInstanceId = playerSessionId.value,
            playerStatus = playerStatus(isPlaying),
            playlistQueryId = burst.playlistId,
            positionInBurst = positionInBurst(),
            stream = stream(),
            totalPlayTime = totalPlayTime(),
            burstId = burst.id,
            playlistId = playlistId,
            userId = userId,
            libraryConfiguration = libraryConfiguration,
            time = occurrenceTime.milliseconds.toLong(),
            playlistName = playlistName,
            pageViewId = playerSessionId.value,
            advertisementEvent = advertisementEvent,
            action = playerAction,
            ctaButtonText = ctaButtonText,
            ctaUrl = ctaUrl,
        )
    }

    private fun PlaybackEvent.stream(): Boolean {
        val currentPlaybackUrl = eventPayload.currentPlaybackUrl ?: return false
        return when {
            currentPlaybackUrl.contains("mp3") -> false
            currentPlaybackUrl.contains("m3u8") -> true
            else -> false
        }
    }

    private fun PlaybackEvent.totalPlayTime(): Double = positionInBurst()

    private fun PlaybackEvent.positionInBurst(): Double = with(eventPayload) {
        if (this@positionInBurst is PlaybackEvent.StartOfPlay) {
            0.0
        } else {
            if (isInAd()) {
                currentPlayBackPosition.seconds
            } else {
                advertisement?.let { currentPlayBackPosition.seconds - it.duration.seconds } ?: currentPlayBackPosition.seconds
            }
        }
    }

    private fun PlaybackEvent.length(): Double = with(eventPayload) {
        if (this@length is PlaybackEvent.StartOfPlay) {
            burst.duration.seconds
        } else {
            if (isInAd()) {
                advertisement?.duration?.seconds ?: 0.0
            } else {
                burst.duration.seconds
            }
        }
    }

    private fun EventPayload.isInAd(): Boolean =
        advertisement?.let {
            currentPlayBackPosition.milliseconds <= it.duration.milliseconds
        } ?: false

    private fun playerStatus(isPlaying: Boolean): PlayerEvent.Status =
        if (isPlaying) PlayerEvent.Status.Playing else PlayerEvent.Status.Stopped
}

private val Event.logMessage: String
    get() = when (this) {
        is GeneralEvent.GetPlaylists -> ""
        is PlaybackEvent -> ", ${eventPayload.burst.id}"
        is GeneralEvent.UiEvent -> ", ${playerEvent.burstId}"
    }
