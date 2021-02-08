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

internal class AudioburstLibraryDelegate(applicationKey: String) {

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

    suspend fun setAudioburstUserID(userId: String): Result<Boolean> = updateUserId(userId)

    fun setAudioburstUserID(userId: String, onData: (Boolean) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            updateUserId(userId)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getPlaylists(): Result<List<PlaylistInfo>> = getPlaylistsInfo()

    fun getPlaylists(onData: (List<PlaylistInfo>) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getPlaylistsInfo()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist> = getPlaylist.invoke(playlistInfo)

    suspend fun getPlaylist(byteArray: ByteArray): Result<Playlist> = getPlaylist.invoke(byteArray)

    fun getPlaylist(playlistInfo: PlaylistInfo, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getPlaylist.invoke(playlistInfo)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun getPlaylist(byteArray: ByteArray, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getPlaylist.invoke(byteArray)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getAdUrl(burst: Burst): Result<String> = getAdUrl.invoke(burst)

    fun getAdUrl(burst: Burst, onData: (String) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getAdUrl.invoke(burst)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun getPersonalPlaylist(): Flow<Result<PendingPlaylist>> = observePersonalPlaylist()

    fun getPersonalPlaylist(onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit) {
        observePersonalPlaylist()
            .onEach { result ->
                result
                    .onData { onData(it) }
                    .onError { onError(it) }
            }
            .launchIn(scope)
    }

    suspend fun getUserPreferences(): Result<UserPreferences> = getUserPreferences.invoke()

    fun getUserPreferences(onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            getUserPreferences.invoke()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun setUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> = postUserPreferences(userPreferences)

    fun setUserPreferences(userPreferences: UserPreferences, onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        scope.launch {
            postUserPreferences(userPreferences)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun start() {
        eventDetector.start()
    }

    fun stop() {
        eventDetector.stop()
    }

    fun setPlaybackStateListener(listener: PlaybackStateListener) {
        eventDetector.setPlaybackStateListener(listener)
    }

    fun removePlaybackStateListener(listener: PlaybackStateListener) {
        eventDetector.removePlaybackStateListener(listener)
    }
}
