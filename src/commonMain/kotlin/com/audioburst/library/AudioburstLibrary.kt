package com.audioburst.library

import com.audioburst.library.models.*
import com.audioburst.library.utils.EventDetector
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
expect class AudioburstLibrary(applicationKey: String)

internal interface CoroutineAudioburstLibrary : EventDetector {

    suspend fun getPlaylists(): Result<List<PlaylistInfo>>

    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Result<Playlist>

    suspend fun getAdUrl(burst: Burst): Result<String>

    suspend fun getPersonalPlaylist(): Flow<Result<PendingPlaylist>>

    suspend fun getUserPreferences(): Result<UserPreferences>

    suspend fun setUserPreferences(userPreferences: UserPreferences): Result<UserPreferences>
}

internal interface CallbackAudioburstLibrary : EventDetector {

    fun getPlaylists(onData: (List<PlaylistInfo>) -> Unit, onError: (LibraryError) -> Unit)

    fun getPlaylist(playlistInfo: PlaylistInfo, onData: (Playlist) -> Unit, onError: (LibraryError) -> Unit)

    fun getAdUrl(burst: Burst, onData: (String) -> Unit, onError: (LibraryError) -> Unit)

    fun getPersonalPlaylist(onData: (PendingPlaylist) -> Unit, onError: (LibraryError) -> Unit)

    fun getUserPreferences(onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit)

    fun setUserPreferences(userPreferences: UserPreferences, onData: (UserPreferences) -> Unit, onError: (LibraryError) -> Unit)
}
