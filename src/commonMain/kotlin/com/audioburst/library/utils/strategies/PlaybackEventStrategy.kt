package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import com.audioburst.library.utils.Queue

internal fun interface PlaybackEventStrategy<T : PlaybackEvent> {

    fun check(input: Input): T?

    data class Input(
        val playlist: Playlist,
        val currentState: InternalPlaybackState,
        val previousStates: Queue<InternalPlaybackState>,
        val advertisements: List<DownloadedAdvertisement>,
    )

    fun Burst.percentProgressOf(playbackState: InternalPlaybackState): Double =
        if (duration.milliseconds == 0.0) 0.0 else playbackState.position / duration.seconds * 100

    fun Burst.isAudioUrl(url: String, advertisements: List<DownloadedAdvertisement>): Boolean =
        url == audioUrl || url == streamUrl || url == advertisement(advertisements)?.audioURL
}

internal fun PlaybackEventStrategy.Input.currentEventPayload(isPlaying: Boolean = true): EventPayload? {
    val currentBurst = currentBurst() ?: return null
    return EventPayload(
        playlistId = playlist.id,
        playlistName = playlist.name,
        burst = currentBurst,
        isPlaying = isPlaying,
        occurrenceTime = currentState.occurrenceTime,
        currentPlayBackPosition = currentState.position.toDuration(DurationUnit.Seconds),
        playerSessionId = playlist.playerSessionId,
    )
}

internal fun PlaybackEventStrategy.Input.lastState(): InternalPlaybackState? = previousStates.lastOrNull()

internal fun PlaybackEventStrategy.Input.indexOfCurrentBurst(): Int = indexOfBurst(currentState)

internal fun PlaybackEventStrategy.Input.indexOfLastBurst(): Int = lastState()?.let(this::indexOfBurst) ?: -1

internal fun PlaybackEventStrategy.Input.currentBurst(): Burst? = burstFromState(currentState)

private fun PlaybackEventStrategy.Input.burstFromState(playbackState: InternalPlaybackState): Burst? =
    playlist.bursts.firstOrNull { burst -> burst.containsUrl(playbackState, advertisements) }

private fun PlaybackEventStrategy.Input.indexOfBurst(playbackState: InternalPlaybackState): Int =
    playlist.bursts.indexOfFirst { burst -> burst.containsUrl(playbackState, advertisements) }

private fun Burst.advertisement(advertisements: List<DownloadedAdvertisement>): Advertisement? =
    adUrl?.let { adUrl ->
        advertisements.firstOrNull {
            adUrl == it.downloadUrl.value
        }
    }?.advertisement

private fun Burst.containsUrl(playbackState: InternalPlaybackState, advertisements: List<DownloadedAdvertisement>): Boolean =
    audioUrl == playbackState.url ||
        streamUrl == playbackState.url ||
        source.audioUrl == playbackState.url ||
        advertisement(advertisements)?.audioURL == playbackState.url
