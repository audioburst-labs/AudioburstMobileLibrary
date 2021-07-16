package com.audioburst.library.interactors

import com.audioburst.library.data.*
import com.audioburst.library.data.repository.PlaylistRepository
import com.audioburst.library.data.storage.ListenedBurstStorage
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*

internal class GetPlaylist(
    private val getUser: GetUser,
    private val playlistRepository: PlaylistRepository,
    private val postContentLoadEvent: PostContentLoadEvent,
    private val playlistStorage: PlaylistStorage,
    private val listenedBurstStorage: ListenedBurstStorage,
    private val userStorage: UserStorage,
) {

    suspend operator fun invoke(playlistInfo: PlaylistInfo): Result<Playlist> =
        getPlaylist { playlistRepository.getPlaylist(it.userId, playlistInfo) }

    suspend operator fun invoke(playlistRequest: PlaylistRequest): Result<Playlist> =
        getPlaylist(playlistRequest.requestOptions) {
            when (playlistRequest) {
                is PlaylistRequest.Account -> playlistRepository.account(playlistRequest, it.userId)
                is PlaylistRequest.Channel -> playlistRepository.channel(playlistRequest, it.userId)
                is PlaylistRequest.Source -> playlistRepository.source(playlistRequest, it.userId)
                is PlaylistRequest.UserGenerated -> playlistRepository.userGenerated(playlistRequest, it.userId)
            }
        }

    private suspend fun getPlaylist(requestOptions: PlaylistRequest.Options? = null, getPlaylistCall: suspend (User) -> Resource<Playlist>): Result<Playlist> =
        getUser().then { user ->
            getPlaylistCall(user).onData {
                postContentLoadEvent(it)
            }
        }.filterListenedBursts(notFilterableBurstId = requestOptions?.firstBurstId)
            .reorderIfNeeded(requestOptions)
            .onData(playlistStorage::setPlaylist)
            .asResult()

    private suspend fun Resource<Playlist>.filterListenedBursts(notFilterableBurstId: String? = null): Resource<Playlist> =
        if (userStorage.filterListenedBursts && this is Resource.Data && this.result.shouldApplyFilter()) {
            map { playlist ->
                val listenedBursts = listenedBurstStorage.getRecentlyListened().map { it.id }.toSet() - notFilterableBurstId
                if (listenedBursts.isEmpty()) {
                    playlist
                } else {
                    val filteredBursts = playlist.bursts.filterNot { listenedBursts.contains(it.id) }
                    val newBurstList = filteredBursts.ifEmpty {
                        playlist.bursts.take(NUMBER_OF_BURST_WHEN_ALL_LISTENED)
                    }
                    playlist.copy(newBurstList)
                }
            }
        } else {
            this
        }

    private fun Resource<Playlist>.reorderIfNeeded(requestOptions: PlaylistRequest.Options?): Resource<Playlist> =
        then { playlist ->
            if (playlist.bursts.isEmpty() || requestOptions == null) {
                return@then Resource.Data(playlist)
            }
            val shuffledPlaylist = if (requestOptions.shuffle) playlist.copy(bursts = playlist.bursts.shuffled()) else playlist
            val firstBurstId = requestOptions.firstBurstId ?: return@then Resource.Data(shuffledPlaylist)
            val firstBurst = shuffledPlaylist.bursts.firstOrNull { it.id == firstBurstId } ?: return@then Resource.Data(shuffledPlaylist)
            val reorderedPlaylist = if (shuffledPlaylist.bursts.indexOf(firstBurst) != 0) {
                val firstRemoved = shuffledPlaylist.bursts - firstBurst
                shuffledPlaylist.copy(bursts = listOf(firstBurst) + firstRemoved)
            } else {
                shuffledPlaylist
            }
            Resource.Data(reorderedPlaylist)
        }

    private fun Playlist.shouldApplyFilter(): Boolean = intent == Playlist.Intent.Playlists

    companion object {
        private const val NUMBER_OF_BURST_WHEN_ALL_LISTENED = 2
    }
}
