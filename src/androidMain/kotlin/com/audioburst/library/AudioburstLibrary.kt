package com.audioburst.library

import com.audioburst.library.models.*
import com.audioburst.library.utils.PlaybackStateListener
import kotlinx.coroutines.flow.Flow

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
actual class AudioburstLibrary actual constructor(applicationKey: String) {

    private val delegate = AudioburstLibraryDelegate(applicationKey)

    /**
     * If you already have users in your app and you wouldn't like to register new one, you can use this function to
     * inform library what ABUserId it should use to communicate to the API. This function will return true if the given
     * ABUserId is correct and present in Audioburst database. Otherwise it will return false.
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun setAudioburstUserID(userId: String): Result<Boolean> =
        delegate.setAudioburstUserID(userId)

    /**
     * Use this function to get all of the available [PlaylistInfo].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getPlaylists(): Result<List<PlaylistInfo>> =
        delegate.getPlaylists()

    /**
     * Use this function to get information about chosen [Playlist].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist> =
        delegate.getPlaylist(playlistInfo)

    /**
     * You can use this function to pass previously recorded and converted to ByteArray search query.
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getPlaylist(byteArray: ByteArray): Result<Playlist> =
        delegate.getPlaylist(byteArray)

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [LibraryError.AdUrlNotFound] error in [Result.Error].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getAdUrl(burst: Burst): Result<String> =
        delegate.getAdUrl(burst)

    /**
     * In case you would like to play Burst's advertisement audio, you should use this function to get URL to play.
     * Note that only [Burst] whose [Burst.isAdAvailable] returns true will return correct URL. Otherwise you will get
     * [LibraryError.AdUrlNotFound] error in [Result.Error].
     * This is the version of the method that accepts [Burst.id] as a parameter.
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getAdUrl(burstId: String): Result<String> =
        delegate.getAdUrl(burstId)

    /**
     * Personal playlist is a special type of playlist that is built with user preferences in mind. Sometimes it takes
     * more time to prepare a personal playlist that is why library exposes an ability to "subscribe" to ongoing changes
     * to personal playlist. By subscribing you will be notified every time there are new [Burst]`s in the playlist until
     * playlist is ready. You can check whether playlist is ready by querying [PendingPlaylist.isReady] value.
     */
    suspend fun getPersonalPlaylist(): Flow<Result<PendingPlaylist>> =
        delegate.getPersonalPlaylist()

    /**
     * You can use this function to pass search query and search for [Burst]s.
     *
     * Returns [Result.Data] when it was possible to get requested resource. When the API returned an empty list of [Burst]s
     * you will get [LibraryError.NoSearchResults]. In case there was a problem getting it [Result.Error] will be returned
     * with a proper error ([LibraryError]).
     */
    suspend fun search(query: String): Result<Playlist> =
        delegate.search(query)

    /**
     * Use this function to get information about user's [UserPreferences].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun getUserPreferences(): Result<UserPreferences> =
        delegate.getUserPreferences()

    /**
     * Use this function to update information about user's [UserPreferences].
     *
     * Returns [Result.Data] when it was possible to get requested resource. In case there was a problem getting it
     * [Result.Error] will be returned with a proper error ([LibraryError]).
     */
    suspend fun setUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> =
        delegate.setUserPreferences(userPreferences)

    /**
     * Call [start] after playback started.
     */
    fun start() {
        delegate.start()
    }

    /**
     * Call [stop] after playback stopped.
     */
    fun stop() {
        delegate.stop()
    }

    /**
     * [Burst] class exposes nullable [CtaData], which you can use to show a CTA (Call to action) button which prompts
     * the user to an immediate response. The CtaData, when available, provides the text to be shown on the button
     * (buttonText) and the link (url) to open in the browser upon clicking the button.
     *
     * When the user clicks this button, you should call the following function to inform the library about this.
     */
    fun ctaButtonClick(burstId: String) {
        delegate.ctaButtonClick(burstId)
    }

    /**
     * Use this function to register new [PlaybackStateListener]. Library will call this interface everytime we need
     * information about what is currently being played. Note that at the same time you can have only one [PlaybackStateListener]
     * registered.
     */
    fun setPlaybackStateListener(listener: PlaybackStateListener) {
        delegate.setPlaybackStateListener(listener)
    }

    /**
     * Use this function to unregister [PlaybackStateListener].
     */
    fun removePlaybackStateListener(listener: PlaybackStateListener) {
        delegate.removePlaybackStateListener(listener)
    }

    /**
     * By default Library will filter-out all already listened by the user Bursts. Use this function to change this behaviour.
     */
    fun filterListenedBursts(enabled: Boolean) {
        delegate.filterListenedBursts(enabled)
    }
}