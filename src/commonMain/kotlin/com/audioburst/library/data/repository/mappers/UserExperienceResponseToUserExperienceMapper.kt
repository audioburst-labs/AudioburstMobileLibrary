package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.PlayerSettingsResponse
import com.audioburst.library.data.repository.models.UserExperienceResponse
import com.audioburst.library.models.PlayerSettings
import com.audioburst.library.models.UserExperience

internal class UserExperienceResponseToUserExperienceMapper(
    private val experiencePlaylistRequestCreator: ExperiencePlaylistRequestCreator,
) {

    fun map(from: UserExperienceResponse): UserExperience =
        UserExperience(
            id = from.id,
            playerSettings = from.playerSettings.toPlayerSettings(),
            request = experiencePlaylistRequestCreator.create(from),
        )

    private fun PlayerSettingsResponse.toPlayerSettings(): PlayerSettings =
        PlayerSettings(
            mode = PlayerSettings.Mode.create(modeName = mode),
            autoplay = autoplay,
            accentColor = accentColor.moveTransparencyCharacterAtTheBeginning(),
            theme = PlayerSettings.Theme.create(themeName = theme),
            isShuffleEnabled = shuffle ?: false,
        )

    /**
     * Because of how the API works, it sends the color String with transparency characters at the
     * end, so we agreed we will fix it on apps side by moving last two characters to the beginning
     * when the text length is equal to 8.
     * Example backend's color notion: "#112233ff"
     */
    private fun String.moveTransparencyCharacterAtTheBeginning(): String {
        val withoutColorPrefix = removePrefix(COLOR_PREFIX)
        return if (withoutColorPrefix.count() == 8) {
            buildString {
                append(COLOR_PREFIX)
                append(withoutColorPrefix.takeLast(2))
                append(withoutColorPrefix.take(6))
            }
        } else {
            this
        }
    }

    companion object {
        private const val COLOR_PREFIX = "#"
    }
}