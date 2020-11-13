package com.audioburst.library

import com.audioburst.library.models.Burst
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.Result
import com.audioburst.library.utils.PlaybackStateListener

internal interface CoroutineAudioburstLibrary {
    /**
     * Use this function to get all of the available [PlaylistInfo].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getPlaylists(): Result<List<PlaylistInfo>>

    /**
     * Use this function to get information about chosen [Playlist].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist>

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [Result.Error.Type.AdUrlNotFound] error in [Result.Error].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getAdUrl(burst: Burst): Result<String>

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