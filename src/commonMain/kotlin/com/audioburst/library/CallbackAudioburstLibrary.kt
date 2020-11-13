package com.audioburst.library

import com.audioburst.library.models.Burst
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.Result
import com.audioburst.library.utils.PlaybackStateListener

internal interface CallbackAudioburstLibrary {
    /**
     * Use this function to get all of the available [PlaylistInfo].
     */
    fun getPlaylists(onData: (List<PlaylistInfo>) -> Unit, onError: (Result.Error.Type) -> Unit)

    /**
     * Use this function to get information about chosen [Playlist].
     */
    fun getPlaylist(playlistInfo: PlaylistInfo, onData: (Playlist) -> Unit, onError: (Result.Error.Type) -> Unit)

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [Result.Error.Type.AdUrlNotFound] error in [Result.Error].
     */
    fun getAdUrl(burst: Burst, onData: (String) -> Unit, onError: (Result.Error.Type) -> Unit)

    /**
     * Call [start] after playback started.
     */
    fun start()

    /**
     * Call [stop] after playback stopped.
     */
    fun stop()

    /**
     * Use this function to register new [PlaybackStateListener]. Library will call this interface everytime we need
     * information about what is currently being played. Note that at the same time you can have only one [PlaybackStateListener]
     * registered.
     */
    fun setPlaybackStateListener(playbackStateListener: PlaybackStateListener)

    /**
     * Use this function to unregister [PlaybackStateListener].
     */
    fun removePlaybackStateListener(playbackStateListener: PlaybackStateListener)
}