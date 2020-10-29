package com.audioburst.library

import com.audioburst.library.di.Injector
import com.audioburst.library.interactors.GetAdUrl
import com.audioburst.library.interactors.GetPlaylist
import com.audioburst.library.interactors.GetPlaylistsInfo
import com.audioburst.library.models.*
import com.audioburst.library.utils.EventDetector
import com.audioburst.library.utils.PlaybackStateListener
import com.audioburst.library.utils.SubscriptionKeySetter

/**
 * Main entry point to the library. In a constructor you should pass applicationKey you can obtain from Audioburst
 * Publishers (https://publishers.audioburst.com/).
 * You can use this class by instantiating its instance every time you need this or as a singleton.
 *
 * Within this class you can get information about all available Playlists, information about specific Playlist and
 * get URL that should be used to play AD.
 * This Library is also responsible for detecting events that are happening during the playback. We use this data to
 * improve user experience. Please register [PlaybackStateListener] to give library information about current [PlaybackState].
 */
class AudioburstLibrary(applicationKey: String) {

    internal lateinit var subscriptionKeySetter: SubscriptionKeySetter
    internal lateinit var getPlaylistsInfo: GetPlaylistsInfo
    internal lateinit var eventDetector: EventDetector
    internal lateinit var getPlaylist: GetPlaylist
    internal lateinit var getAdUrl: GetAdUrl

    init {
        Injector.inject(this)
        subscriptionKeySetter.set(SubscriptionKey(applicationKey))
    }

    /**
     * Use this function to get all of the available [PlaylistInfo].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getPlaylists(): Result<List<PlaylistInfo>> = getPlaylistsInfo()

    /**
     * Use this function to get information about chosen [Playlist].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist> = getPlaylist.invoke(playlistInfo)

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [Result.Error.Type.AdUrlNotFound] error in [Result.Error].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([Result.Error.Type]).
     */
    suspend fun getAdUrl(burst: Burst): Result<String> = getAdUrl.invoke(burst)

    /**
     * Call [start] after playback started.
     */
    fun start() {
        eventDetector.start()
    }

    /**
     * Call [stop] after playback stopped.
     */
    fun stop() {
        eventDetector.stop()
    }

    /**
     * Use this function to register new [PlaybackStateListener]. Library will call this interface everytime we need
     * information about what is currently being played. Note that at the same time you can have only one [PlaybackStateListener]
     * registered.
     */
    fun setPlaybackStateListener(playbackStateListener: PlaybackStateListener) {
        eventDetector.setPlaybackStateListener(playbackStateListener)
    }

    /**
     * Use this function to unregister [PlaybackStateListener].
     */
    fun removePlaybackStateListener(playbackStateListener: PlaybackStateListener) {
        eventDetector.removePlaybackStateListener(playbackStateListener)
    }
}
