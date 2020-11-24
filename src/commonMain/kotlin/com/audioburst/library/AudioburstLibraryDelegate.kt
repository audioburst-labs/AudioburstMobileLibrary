package com.audioburst.library

import com.audioburst.library.di.Injector
import com.audioburst.library.interactors.*
import com.audioburst.library.models.*
import com.audioburst.library.utils.PlaybackStateListener
import com.audioburst.library.utils.StrategyBasedEventDetector
import com.audioburst.library.utils.SubscriptionKeySetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class AudioburstLibraryDelegate(applicationKey: String) : CoroutineAudioburstLibrary, CallbackAudioburstLibrary {

    internal lateinit var observePersonalPlaylist: ObservePersonalPlaylist
    internal lateinit var subscriptionKeySetter: SubscriptionKeySetter
    internal lateinit var postUserPreferences: PostUserPreferences
    internal lateinit var getUserPreferences: GetUserPreferences
    internal lateinit var getPlaylistsInfo: GetPlaylistsInfo
    internal lateinit var appDispatchers: AppDispatchers
    internal lateinit var eventDetector: StrategyBasedEventDetector
    internal lateinit var updateUserId: UpdateUserId
    internal lateinit var getPlaylist: GetPlaylist
    internal lateinit var getAdUrl: GetAdUrl

    private val scope: CoroutineScope by lazy { CoroutineScope(appDispatchers.main + SupervisorJob()) }

    init {
        Injector.inject(this)
        subscriptionKeySetter.set(SubscriptionKey(applicationKey))
    }

    /**
     * If you already have users in your app and you wouldn't like to register new one, you can use this function to
     * inform library what ABUserId it should use to communicate to the API. This function will return true if the given
     * ABUserId is correct and present in Audioburst database. Otherwise it will return false.
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun setAudioburstUserID(userId: String): Result<Boolean> = updateUserId(userId)

    /**
     * If you already have users in your app and you wouldn't like to register new one, you can use this function to
     * inform library what ABUserId it should use to communicate to the API. This function will return true if the given
     * ABUserId is correct and present in Audioburst database. Otherwise it will return false.
     */
    override fun setAudioburstUserID(userId: String, onData: (Boolean) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            updateUserId(userId)
                .onData { onData(it) }
                .onError { onError(it) }
        }
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
     * Personal playlist is a special type of playlist that is built with user preferences in mind. Sometimes it takes
     * more time to prepare a personal playlist that is why library exposes an ability to "subscribe" to ongoing changes
     * to personal playlist. By subscribing you will be notified every time there are new [Burst]`s in the playlist until
     * playlist is ready. You can check whether playlist is ready by querying [PendingPlaylist.isReady] value.
     */
    override suspend fun getPersonalPlaylist(): Flow<Result<PendingPlaylist>> = observePersonalPlaylist()

    /**
     * Personal playlist is a special type of playlist that is built with user preferences in mind. Sometimes it takes
     * more time to prepare a personal playlist that is why library exposes an ability to "subscribe" to ongoing changes
     * to personal playlist. [onData] callback will be called every time there are new [Burst]`s in the playlist until
     * playlist is ready. You can check whether playlist is ready by querying [PendingPlaylist.isReady] value.
     */
    override fun getPersonalPlaylist(onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit) {
        observePersonalPlaylist()
            .onEach { result ->
                result
                    .onData { onData(it) }
                    .onError { onError(it) }
            }
            .launchIn(scope)
    }

    /**
     * Use this function to get information about user's [UserPreferences].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun getUserPreferences(): Result<UserPreferences> = getUserPreferences.invoke()

    /**
     * Use this function to get information about user's [UserPreferences].
     */
    override fun getUserPreferences(onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getUserPreferences.invoke()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    /**
     * Use this function to update information about user's [UserPreferences].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    override suspend fun setUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> = postUserPreferences(userPreferences)

    /**
     * Use this function to update information about user's [UserPreferences].
     */
    override fun setUserPreferences(userPreferences: UserPreferences, onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            postUserPreferences(userPreferences)
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
