package com.audioburst.library.models

import com.audioburst.library.utils.Queue

internal data class AnalysisInput(
    val playlist: Playlist,
    val currentState: InternalPlaybackState,
    val previousStates: Queue<InternalPlaybackState>,
    val advertisements: List<DownloadedAdvertisement>,
)

internal fun AnalysisInput.currentEventPayload(burst: Burst? = null, isPlaying: Boolean = true): EventPayload? =
    eventPayload(currentState, burst, isPlaying)

internal fun AnalysisInput.eventPayload(playbackState: InternalPlaybackState, burst: Burst? = null, isPlaying: Boolean = true): EventPayload? {
    if (burst != null && playlist.containsBurst(burst)) {
        return null
    }
    val currentBurst = burst ?: burstFromState(playbackState) ?: return null
    return EventPayload(
        playerAction = playlist.playerAction,
        playlistId = playlist.id,
        playlistName = playlist.name,
        burst = currentBurst,
        isPlaying = isPlaying,
        occurrenceTime = playbackState.occurrenceTime,
        currentPlayBackPosition = playbackState.position,
        playerSessionId = playlist.playerSessionId,
        advertisement = currentBurst.advertisement(advertisements),
        currentPlaybackUrl = playbackState.url,
    )
}

private fun Playlist.containsBurst(burst: Burst): Boolean = bursts.firstOrNull { it.id == burst.id } == null

internal fun AnalysisInput.lastState(): InternalPlaybackState? = previousStates.lastOrNull()

internal fun AnalysisInput.indexOfCurrentBurst(): Int = indexOfBurst(currentState)

internal fun AnalysisInput.indexOfLastBurst(): Int = lastState()?.let(this::indexOfBurst) ?: -1

internal fun AnalysisInput.currentBurst(): Burst? = burstFromState(currentState)

private fun AnalysisInput.burstFromState(playbackState: InternalPlaybackState): Burst? =
    playlist.bursts.firstOrNull { burst -> burst.containsUrl(playbackState, advertisements) }

private fun AnalysisInput.indexOfBurst(playbackState: InternalPlaybackState): Int =
    playlist.bursts.indexOfFirst { burst -> burst.containsUrl(playbackState, advertisements) }

internal fun Burst.advertisement(advertisements: List<DownloadedAdvertisement>): Advertisement? =
    adUrl?.let { adUrl ->
        advertisements.firstOrNull {
            adUrl == it.downloadUrl.value
        }
    }?.advertisement

private fun Burst.containsUrl(playbackState: InternalPlaybackState, advertisements: List<DownloadedAdvertisement>): Boolean =
    audioUrl == playbackState.url ||
        streamUrl == playbackState.url ||
        source.audioUrl == playbackState.url ||
        advertisement(advertisements)?.burstUrl == playbackState.url