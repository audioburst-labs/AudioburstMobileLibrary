package com.audioburst.library.interactors

import com.audioburst.library.models.*
import com.audioburst.library.utils.TimestampProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class PostContentLoadEvent(
    private val playbackEventHandler: PlaybackEventHandler,
    private val timestampProvider: TimestampProvider,
) {

    /**
     * Launching new coroutine, because we would like it to be a fire and forget action
     */
    suspend operator fun invoke(playlist: Playlist) {
        coroutineScope {
            launch {
                playlist.toEventPayload(
                    occurrenceTime = timestampProvider.currentTimeMillis()
                )?.let(PlaybackEvent::ContentLoaded)?.let { playbackEventHandler.handle(it) }
            }
        }
    }
}

private fun Playlist.toEventPayload(
    isPlaying: Boolean = false,
    currentPlayBackPosition: Duration = 0.0.toDuration(DurationUnit.Seconds),
    occurrenceTime: Long,
): EventPayload? {
    val burst = bursts.firstOrNull() ?: return null
    return EventPayload(
        playerAction = playerAction,
        playlistId = id,
        playlistName = name,
        burst = burst,
        isPlaying = isPlaying,
        occurrenceTime = occurrenceTime,
        currentPlayBackPosition = currentPlayBackPosition,
        playerSessionId = playerSessionId,
        advertisement = null,
    )
}
