package com.audioburst.library.models

internal sealed class Event {
    abstract val actionName: String
}

internal sealed class PlaybackEvent(override val actionName: String, val eventPayload: EventPayload) : Event() {

    class Play(eventPayload: EventPayload) : PlaybackEvent("play", eventPayload)
    class Playing(eventPayload: EventPayload) : PlaybackEvent("playing", eventPayload)
    class Skip(eventPayload: EventPayload) : PlaybackEvent("skip", eventPayload)
    class Pause(eventPayload: EventPayload) : PlaybackEvent("pause", eventPayload)
    class Repeat(eventPayload: EventPayload) : PlaybackEvent("repeat", eventPayload)
    class Back(eventPayload: EventPayload) : PlaybackEvent("back", eventPayload)
    class StartOfPlay(eventPayload: EventPayload) : PlaybackEvent("startOfPlay", eventPayload)
    class EndOfPlay(eventPayload: EventPayload) : PlaybackEvent("endOfPlay", eventPayload)
    class Skip30Sec(eventPayload: EventPayload) : PlaybackEvent("30secSkip", eventPayload)
    class BackSec30(eventPayload: EventPayload) : PlaybackEvent("30secBack", eventPayload)
    class KeepListening(eventPayload: EventPayload) : PlaybackEvent("keepListening", eventPayload)
    class BackToBurst(eventPayload: EventPayload) : PlaybackEvent("backToBurst", eventPayload)
    class Forward(eventPayload: EventPayload) : PlaybackEvent("forward", eventPayload)
    class Rewind(eventPayload: EventPayload) : PlaybackEvent("rewind", eventPayload)
    class EndOfPlaylist(eventPayload: EventPayload) : PlaybackEvent("endOfPlaylist", eventPayload)
    class TwoSecPlaying(eventPayload: EventPayload) : PlaybackEvent("2secPlaying", eventPayload)
    class TwoSecADPlaying(eventPayload: EventPayload) : PlaybackEvent("2secADPlaying", eventPayload)
    class AdListened(
        val advertisement: Advertisement,
        val reportingData: ReportingData,
        eventPayload: EventPayload,
    ) : PlaybackEvent("AD", eventPayload)
    class ContentLoaded(eventPayload: EventPayload) : PlaybackEvent("contentLoad", eventPayload)
    class BurstListened(eventPayload: EventPayload) : PlaybackEvent("BurstListened", eventPayload)
    class CtaClick(eventPayload: EventPayload, val buttonText: String, val url: String) : PlaybackEvent("cta-click", eventPayload)
}

internal sealed class GeneralEvent(override val actionName: String) : Event() {
    class GetPlaylists : GeneralEvent("getPlaylists")
}