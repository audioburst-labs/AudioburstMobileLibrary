package com.audioburst.library.interactors

import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration

internal class UiEventHandler(
    private val userStorage: UserStorage,
    private val playbackEventHandler: PlaybackEventHandler,
    private val libraryConfiguration: LibraryConfiguration,
) {

    suspend fun handle(uiEvent: UiEvent, burstId: String, isPlaying: Boolean, analysisInput: AnalysisInput) {
        val playerEvent = playerEvent(
            uiEvent = uiEvent,
            burstId = burstId,
            isPlaying = isPlaying,
            analysisInput = analysisInput,
        ) ?: return

        playbackEventHandler.handle(
            GeneralEvent.UiEvent(
                actionName = uiEvent.eventName,
                playerEvent = playerEvent,
            )
        )
    }

    private fun playerEvent(uiEvent: UiEvent, burstId: String, isPlaying: Boolean, analysisInput: AnalysisInput): PlayerEvent? {
        val userId = userStorage.userId ?: return null
        val currentPlaylist = analysisInput.playlist
        val burst = currentPlaylist.bursts.firstOrNull { it.id == burstId } ?: return null
        val ctaData = if (uiEvent == UiEvent.SkipAdClick) burst.ctaData else null

        return PlayerEvent(
            userId = userId,
            burstId = burstId,
            libraryConfiguration = libraryConfiguration,
            time = analysisInput.currentState.occurrenceTime.milliseconds.toLong(),
            playlistName = currentPlaylist.name,
            burstLength = burst.duration.seconds,
            playerInstanceId = currentPlaylist.playerSessionId.value,
            playerStatus = playerStatus(isPlaying),
            playlistQueryId = burst.playlistId,
            playlistId = currentPlaylist.id,
            positionInBurst = analysisInput.positionInBurst(burst),
            stream = analysisInput.stream(burst),
            totalPlayTime = burst.duration.seconds,
            pageViewId = currentPlaylist.playerSessionId.value,
            advertisementEvent = null,
            action = currentPlaylist.playerAction,
            ctaButtonText = ctaData?.buttonText,
            ctaUrl = ctaData?.url,
        )
    }

    private fun AnalysisInput.isDefaultState(): Boolean {
        val defaultPlaybackState = PlaybackState.DEFAULT
        return currentState.url == defaultPlaybackState.url && currentState.position.milliseconds.toLong() == defaultPlaybackState.positionMillis
    }

    private fun AnalysisInput.positionInBurst(burstFromId: Burst): Double? =
        if (!isDefaultState() && burstFromId == currentBurst()) {
            currentState.position.seconds
        } else {
            null
        }

    private fun AnalysisInput.stream(burstFromId: Burst): Boolean? =
        if (!isDefaultState() && burstFromId == currentBurst()) {
            !currentState.url.contains("mp3") && currentState.url.contains("m3u8")
        } else {
            null
        }

    private fun playerStatus(isPlaying: Boolean): PlayerEvent.Status =
        if (isPlaying) PlayerEvent.Status.Playing else PlayerEvent.Status.Stopped
}