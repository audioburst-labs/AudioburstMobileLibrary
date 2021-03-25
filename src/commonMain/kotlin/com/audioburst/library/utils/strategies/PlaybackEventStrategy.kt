package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*

internal fun interface PlaybackEventStrategy<T : PlaybackEvent> {

    fun check(input: AnalysisInput): T?

    fun Burst.percentProgressOf(playbackState: InternalPlaybackState): Double =
        if (duration.milliseconds == 0.0) 0.0 else playbackState.position.seconds / duration.seconds * 100

    fun Burst.isAudioUrl(url: String, advertisements: List<DownloadedAdvertisement>): Boolean =
        url == audioUrl || url == streamUrl || url == advertisement(advertisements)?.burstUrl
}
