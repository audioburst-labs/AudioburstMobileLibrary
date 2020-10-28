package com.audioburst.library

import com.audioburst.library.data.Resource
import com.audioburst.library.di.Injector
import com.audioburst.library.interactors.GetAdData
import com.audioburst.library.interactors.GetPlaylist
import com.audioburst.library.interactors.GetPlaylistsInfo
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.PlaylistInfo
import com.audioburst.library.models.SubscriptionKey
import com.audioburst.library.utils.EventDetector
import com.audioburst.library.utils.PlaybackStateListener
import com.audioburst.library.utils.SubscriptionKeySetter

class AudioburstLibrary(applicationKey: String) {

    internal lateinit var subscriptionKeySetter: SubscriptionKeySetter
    internal lateinit var getPlaylistsInfo: GetPlaylistsInfo
    internal lateinit var eventDetector: EventDetector
    internal lateinit var getPlaylist: GetPlaylist
    internal lateinit var getAdData: GetAdData

    init {
        Injector.inject(this)
        subscriptionKeySetter.set(SubscriptionKey(applicationKey))
    }

    suspend fun getPlaylists(): Resource<List<PlaylistInfo>> = getPlaylistsInfo()

    suspend fun getPlaylist(playlistInfo: PlaylistInfo): Resource<Playlist> = getPlaylist.invoke(playlistInfo)

    fun play() {
        eventDetector.play()
    }

    fun pause() {
        eventDetector.pause()
    }

    fun setPlaybackStateListener(playbackStateListener: PlaybackStateListener) {
        eventDetector.setPlaybackStateListener(playbackStateListener)
    }

    fun removePlaybackStateListener(playbackStateListener: PlaybackStateListener) {
        eventDetector.removePlaybackStateListener(playbackStateListener)
    }
}
