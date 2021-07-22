package com.audioburst.library.models

enum class UiEvent(internal val eventName: String) {
    /**
     * User clicked on the button that shows a list of Bursts.
     */
    PlaylistClick("playlistClick"),
    /**
     * User clicked on the button that hides a list of Bursts.
     */
    PlaylistClose("playlistClose"),
    /**
     * User clicked on the item that represents a Burst in a playlist.
     */
    SelectItemInPlaylist("SelectItemInPlaylist"),
    /**
     * User tapped on like.
     */
    ThumbsUp("thumbsUp"),
    /**
     * User tapped on dislike.
     */
    ThumbsDown("thumbsDown"),
    /**
     * User activated player for the first time in this session.
     */
    PlayerActivation("playerActivation"),
    /**
     * User tapped a skip ad button.
     */
    SkipAdClick("skip-ad-click"),
    /**
     * User tapped a close button on the Button Player.
     */
    PlayerRemoved("PlayerRemoved"),
    /**
     * User tapped a share button.
     */
    ShareBurst("shareBurst"),
}