package com.audioburst.library

import com.audioburst.library.di.Injector
import com.audioburst.library.interactors.GetAdUrl
import com.audioburst.library.interactors.GetPlaylist
import com.audioburst.library.interactors.GetPlaylistsInfo
import com.audioburst.library.models.*
import com.audioburst.library.utils.PlaybackStateListener
import com.audioburst.library.utils.StrategyBasedEventDetector
import com.audioburst.library.utils.SubscriptionKeySetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class AudioburstLibraryDelegate(applicationKey: String) : CoroutineAudioburstLibrary, CallbackAudioburstLibrary {

    internal lateinit var subscriptionKeySetter: SubscriptionKeySetter
    internal lateinit var getPlaylistsInfo: GetPlaylistsInfo
    internal lateinit var appDispatchers: AppDispatchers
    internal lateinit var eventDetector: StrategyBasedEventDetector
    internal lateinit var getPlaylist: GetPlaylist
    internal lateinit var getAdUrl: GetAdUrl

    private val scope: CoroutineScope by lazy { CoroutineScope(appDispatchers.main + SupervisorJob()) }

    init {
        Injector.inject(this)
        subscriptionKeySetter.set(SubscriptionKey(applicationKey))
    }

    /**
     * Use this function to get all of the available [PlaylistInfo].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun getPlaylists(): Result<List<PlaylistInfo>> = getPlaylistsInfo()

    /**
     * Use this function to get all of the available [PlaylistInfo].
     */
    override fun getPlaylists(onData: (List<PlaylistInfo>) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getPlaylistsInfo()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    /**
     * Use this function to get information about chosen [Playlist].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist> = getPlaylist.invoke(playlistInfo)

    /**
     * Use this function to get information about chosen [Playlist].
     */
    override fun getPlaylist(playlistInfo: PlaylistInfo, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getPlaylist.invoke(playlistInfo)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [LibraryError.AdUrlNotFound] error in [Result.Error].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun getAdUrl(burst: Burst): Result<String> = getAdUrl.invoke(burst)

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [LibraryError.AdUrlNotFound] error in [Result.Error].
     */
    override fun getAdUrl(burst: Burst, onData: (String) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getAdUrl.invoke(burst)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    /**
     * Call [start] after playback started.
     */
    override fun start() {
        eventDetector.start()
    }

    /**
     * Call [stop] after playback stopped.
     */
    override fun stop() {
        eventDetector.stop()
    }

    /**
     * Use this function to register new [PlaybackStateListener]. Library will call this interface everytime we need
     * information about what is currently being played. Note that at the same time you can have only one [PlaybackStateListener]
     * registered.
     */
    override fun setPlaybackStateListener(listener: PlaybackStateListener) {
        eventDetector.setPlaybackStateListener(listener)
    }

    /**
     * Use this function to unregister [PlaybackStateListener].
     */
    override fun removePlaybackStateListener(listener: PlaybackStateListener) {
        eventDetector.removePlaybackStateListener(listener)
    }
}
