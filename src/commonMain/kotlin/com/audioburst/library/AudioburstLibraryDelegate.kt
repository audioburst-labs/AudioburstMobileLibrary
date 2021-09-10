package com.audioburst.library

import com.audioburst.library.di.Injector
import com.audioburst.library.interactors.*
import com.audioburst.library.models.*
import com.audioburst.library.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class AudioburstLibraryDelegate(applicationKey: String) {

    internal lateinit var enableListenedBurstFiltering: EnableListenedBurstFiltering
    internal lateinit var removeOldListenedBursts: RemoveOldListenedBursts
    internal lateinit var observePersonalPlaylist: ObservePersonalPlaylist
    internal lateinit var subscriptionKeySetter: SubscriptionKeySetter
    internal lateinit var postUserPreferences: PostUserPreferences
    internal lateinit var getUserPreferences: GetUserPreferences
    internal lateinit var getUserExperience: GetUserExperience
    internal lateinit var getPlaylistsInfo: GetPlaylistsInfo
    internal lateinit var getShareOptions: GetShareOptions
    internal lateinit var eventDetector: StrategyBasedEventDetector
    internal lateinit var sdkInfoSetter: SdkInfoSetter
    internal lateinit var updateUserId: UpdateUserId
    internal lateinit var getPlaylist: GetPlaylist
    internal lateinit var getAdUrl: GetAdUrl
    internal lateinit var search: Search
    internal lateinit var scope: CoroutineScope

    init {
        Injector.inject(this)
        subscriptionKeySetter.set(SubscriptionKey(applicationKey))
        scope.launch { removeOldListenedBursts() }
    }

    suspend fun setAudioburstUserID(userId: String): Result<Boolean> {
        Logger.i("setAudioburstUserID")
        return updateUserId(userId)
    }

    fun setAudioburstUserID(userId: String, onData: (Boolean) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("setAudioburstUserID")
        scope.launch {
            updateUserId(userId)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getPlaylists(): Result<List<PlaylistInfo>> {
        Logger.i("getPlaylists")
        eventDetector.getPlaylists()
        return getPlaylistsInfo()
    }

    fun getPlaylists(onData: (List<PlaylistInfo>) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getPlaylists")
        eventDetector.getPlaylists()
        scope.launch {
            getPlaylistsInfo()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist> {
        Logger.i("getPlaylist")
        return getPlaylist.invoke(playlistInfo)
    }

    fun search(byteArray: ByteArray): Flow<Result<PendingPlaylist>> {
        Logger.i("search")
        return search.invoke(byteArray)
    }

    suspend fun getPlaylist(playlistRequest: PlaylistRequest): Result<Playlist> {
        Logger.i("getPlaylist")
        return getPlaylist.invoke(playlistRequest)
    }

    fun getPlaylist(playlistInfo: PlaylistInfo, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getPlaylist")
        scope.launch {
            getPlaylist.invoke(playlistInfo)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun search(byteArray: ByteArray, onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("search")
        search.invoke(byteArray)
            .onEach { result ->
                result
                    .onData { onData(it) }
                    .onError { onError(it) }
            }
            .launchIn(scope)
    }

    fun getPlaylist(playlistRequest: PlaylistRequest, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getPlaylist")
        scope.launch {
            getPlaylist.invoke(playlistRequest)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getAdUrl(burst: Burst): Result<String> {
        Logger.i("getAdUrl")
        return getAdUrl.invoke(burst)
    }

    suspend fun getAdUrl(burstId: String): Result<String> {
        Logger.i("getAdUrl")
        return getAdUrl.invoke(burstId)
    }

    fun getAdUrl(burst: Burst, onData: (String) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getAdUrl")
        scope.launch {
            getAdUrl.invoke(burst)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun getAdUrl(burstId: String, onData: (String) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getAdUrl")
        scope.launch {
            getAdUrl.invoke(burstId)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    fun getPersonalPlaylist(): Flow<Result<PendingPlaylist>> {
        Logger.i("getPersonalPlaylist")
        return observePersonalPlaylist()
    }

    fun getPersonalPlaylist(onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getPersonalPlaylist")
        observePersonalPlaylist()
            .onEach { result ->
                result
                    .onData { onData(it) }
                    .onError { onError(it) }
            }
            .launchIn(scope)
    }

    fun search(query: String): Flow<Result<PendingPlaylist>> {
        Logger.i("search")
        return search.invoke(query)
    }

    fun search(query: String, onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("search")
        search.invoke(query)
            .onEach { result ->
                result
                    .onData { onData(it) }
                    .onError { onError(it) }
            }
            .launchIn(scope)
    }

    suspend fun getUserPreferences(): Result<UserPreferences> {
        Logger.i("getUserPreferences")
        return getUserPreferences.invoke()
    }

    fun getUserPreferences(onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getUserPreferences")
        scope.launch {
            getUserPreferences.invoke()
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun setUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> {
        Logger.i("setUserPreferences")
        return postUserPreferences(userPreferences)
    }

    fun setUserPreferences(userPreferences: UserPreferences, onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("setUserPreferences")
        scope.launch {
            postUserPreferences(userPreferences)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getUserExperience(applicationKey: String, experienceId: String): Result<UserExperience> {
        Logger.i("getUserExperience")
        return getUserExperience.invoke(applicationKey = applicationKey, experienceId = experienceId)
    }

    fun getUserExperience(applicationKey: String, experienceId: String, onData: (UserExperience) -> Unit, onError: (LibraryError) -> Unit) {
        Logger.i("getUserExperience")
        scope.launch {
            getUserExperience.invoke(applicationKey = applicationKey, experienceId = experienceId)
                .onData { onData(it) }
                .onError { onError(it) }
        }
    }

    suspend fun getShareOptions(burstId: String): ShareOptions? {
        Logger.i("getShareOptions")
        return getShareOptions.invoke(burstId)
    }

    fun getShareOptions(burstId: String, onData: (ShareOptions?) -> Unit) {
        Logger.i("getShareOptions")
        scope.launch {
            onData(getShareOptions.invoke(burstId))
        }
    }

    fun start() {
        Logger.i("start")
        eventDetector.start()
    }

    fun stop() {
        Logger.i("stop")
        eventDetector.stop()
    }

    fun ctaButtonClick(burstId: String) {
        Logger.i("ctaButtonClick")
        eventDetector.ctaButtonClick(burstId)
    }

    fun report(uiEvent: UiEvent, burstId: String, isPlaying: Boolean) {
        Logger.i("report: ${uiEvent.eventName}")
        eventDetector.report(uiEvent, burstId, isPlaying)
    }

    fun setPlaybackStateListener(listener: PlaybackStateListener) {
        Logger.i("setPlaybackStateListener")
        eventDetector.setPlaybackStateListener(listener)
    }

    fun removePlaybackStateListener(listener: PlaybackStateListener) {
        Logger.i("removePlaybackStateListener")
        eventDetector.removePlaybackStateListener(listener)
    }

    fun filterListenedBursts(enabled: Boolean) {
        Logger.i("filterListenedBursts")
        enableListenedBurstFiltering(enabled)
    }

    fun setSdkInfo(level: SdkLevel, version: String) {
        Logger.i("setSdkInformation")
        sdkInfoSetter.set(SdkInfo(level = level, version = version))
    }
}
