package com.audioburst.library.data.storage

import com.audioburst.library.models.Advertisement
import com.audioburst.library.models.DownloadedAdvertisement
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Url

internal interface PlaylistStorage {

    /**
     * Holds recently downloaded Playlist
     */
    val currentPlaylist: Playlist?

    /**
     * Contains Advertisements that belongs to the currently downloaded Playlist
     */
    val currentAds: List<DownloadedAdvertisement>

    fun setPlaylist(playlist: Playlist)

    fun setAdvertisement(url: Url, advertisement: Advertisement)
}

internal object InMemoryPlaylistStorage : PlaylistStorage {

    private var _currentPlaylist: Playlist? = null
    override val currentPlaylist: Playlist?
        get() = _currentPlaylist

    private val _currentAds: MutableList<DownloadedAdvertisement> = mutableListOf()
    override val currentAds: List<DownloadedAdvertisement>
        get() = _currentAds

    override fun setPlaylist(playlist: Playlist) {
        _currentPlaylist = playlist
        _currentAds.clear()
    }

    override fun setAdvertisement(url: Url, advertisement: Advertisement) {
        if (_currentPlaylist?.bursts?.mapNotNull { it.adUrl }?.contains(url.value) == true) {
            _currentAds.add(
                DownloadedAdvertisement(
                    downloadUrl = url,
                    advertisement = advertisement
                )
            )
        }
    }

    fun clear() {
        _currentPlaylist = null
        _currentAds.clear()
    }
}
