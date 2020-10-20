package com.audioburst.library.interactors

import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.Playlist

internal fun interface CurrentPlaylist {

    operator fun invoke(): Playlist?
}

internal class CurrentPlaylistInteractor(
    private val playlistStorage: PlaylistStorage
) : CurrentPlaylist {

    override fun invoke(): Playlist? = playlistStorage.currentPlaylist
}
