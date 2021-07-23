package com.audioburst.library.interactors

import com.audioburst.library.data.repository.AppSettingsRepository
import com.audioburst.library.data.repository.PlaylistRepository
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.result
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.Strings

internal class GetShareOptions(
    private val userStorage: UserStorage,
    private val currentPlaylist: CurrentPlaylist,
    private val appSettingsRepository: AppSettingsRepository,
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository,
) {

    suspend operator fun invoke(burstId: String): ShareOptions? {
        val playlist = currentPlaylist() ?: return null
        val burst = playlist.bursts.firstOrNull { it.id == burstId } ?: return null
        val shareTexts = appSettingsRepository.getShareTexts()
        val burstShareUrl = userRepository.getBurstShareUrl(burst).result() ?: return null
        val playlistShareUrl = userStorage.userId?.let { userId ->
            playlistRepository.url(userId = userId, playerAction = playlist.playerAction)
        }

        return ShareOptions(
            burst = ShareData(
                title = shareTexts.burstText(burst, burstShareUrl),
                message = Strings.shareBurstMessage(burst.title),
                url = burstShareUrl.shortUrl,
            ),
            playlist = playlistShareUrl?.value?.let { url ->
                ShareData(
                    title = shareTexts.playlistText(playlist, url),
                    message = Strings.sharePlaylistMessage(playlist.name),
                    url = url,
                )
            }
        )
    }

    private fun ShareTexts.burstText(burst: Burst, burstShareUrl: BurstShareUrl): String =
        this.burst.takeIf { it.containsUrlPlaceholder() }
            ?.replace(NAME_PLACEHOLDER, burst.title)
            ?.replace(URL_PLACEHOLDER, burstShareUrl.shortUrl) ?: burstShareUrl.shortUrl

    private fun ShareTexts.playlistText(playlist: Playlist, shareUrl: String): String =
        this.playlist.takeIf { it.containsUrlPlaceholder() }
            ?.replace(NAME_PLACEHOLDER, playlist.name)
            ?.replace(URL_PLACEHOLDER, shareUrl) ?: shareUrl

    private fun String.containsUrlPlaceholder(): Boolean = contains(URL_PLACEHOLDER)

    companion object {
        private const val NAME_PLACEHOLDER = Strings.namePlaceholder
        private const val URL_PLACEHOLDER = Strings.urlPlaceholder
    }
}