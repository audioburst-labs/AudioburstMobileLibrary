package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.PlayerSettingsResponse
import com.audioburst.library.data.repository.models.UserExperienceResponse
import com.audioburst.library.models.PlayerSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class UserExperienceResponseToUserExperienceTest {

    private val creator = experiencePlaylistRequestCreatorOf()
    private val mapper = UserExperienceResponseToUserExperienceMapper(experiencePlaylistRequestCreator = creator)

    @Test
    fun `test mapper`() {
        // GIVEN
        val response = userExperienceResponseOf()

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(mapped.id, response.id)
        assertEquals(mapped.playerSettings.autoplay, response.playerSettings.autoplay)
        assertEquals(mapped.playerSettings.isShuffleEnabled, response.playerSettings.shuffle)
        assertEquals(mapped.playerSettings.theme, PlayerSettings.Theme.Light)
        assertEquals(mapped.playerSettings.mode, PlayerSettings.Mode.Banner)
        assertEquals(mapped.playerSettings.accentColor, response.playerSettings.accentColor)
    }

    @Test
    fun `test if colorParser parses color String with correct transparency character order`() {
        // GIVEN
        val accentColor = "#4ff580ff"
        val accentColorWithCorrectedTransparencyCharacters = "#ff4ff580"
        val response = userExperienceResponseOf(
            playerSettings = playerSettingsResponseOf(
                accentColor = accentColor,
            )
        )

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(accentColorWithCorrectedTransparencyCharacters, mapped.playerSettings.accentColor)
    }

    @Test
    fun `test if colorParser parses color String without changes if there are no transparency characters`() {
        // GIVEN
        val accentColor = "#4ff580"
        val accentColorWithCorrectedTransparencyCharacters = "#4ff580"
        val response = userExperienceResponseOf(
            playerSettings = playerSettingsResponseOf(
                accentColor = accentColor,
            )
        )

        // WHEN
        val mapped = mapper.map(response)

        // THEN
        assertEquals(accentColorWithCorrectedTransparencyCharacters, mapped.playerSettings.accentColor)
    }

    @Test
    fun `test if correct PlayerMode is created`() {
        // GIVEN
        val modes = listOf("mini", "banner", "floating", "button", "")
        val responses = modes.map { mode ->
            userExperienceResponseOf(
                playerSettings = playerSettingsResponseOf(
                    mode = mode,
                )
            )
        }
        val correctPlayerModeTypes = listOf(
            PlayerSettings.Mode.Banner,
            PlayerSettings.Mode.Banner,
            PlayerSettings.Mode.Button,
            PlayerSettings.Mode.Button,
            PlayerSettings.Mode.Banner,
        )

        // WHEN
        val mapped = responses.map(mapper::map)

        // THEN
        mapped.forEachIndexed { index, experience ->
            val mode = experience.playerSettings.mode
            assertEquals(correctPlayerModeTypes[index], mode)
        }
    }
}

internal fun experiencePlaylistRequestCreatorOf(): ExperiencePlaylistRequestCreator = ExperiencePlaylistRequestCreator()

internal fun userExperienceResponseOf(
    userId: String = "",
    id: String = "",
    settingsId: String = "",
    name: String = "",
    playlist: Int = 0,
    playerSettings: PlayerSettingsResponse = playerSettingsResponseOf(),
    PlayerAction: String = "",
    PlayerActionValue: String? = null,
): UserExperienceResponse =
    UserExperienceResponse(
        userId = userId,
        id = id,
        settingsId = settingsId,
        name = name,
        playlist = playlist,
        playerSettings = playerSettings,
        PlayerAction = PlayerAction,
        PlayerActionValue = PlayerActionValue,
    )

internal fun playerSettingsResponseOf(
    mode: String = "",
    autoplay: Boolean = false,
    accentColor: String = "",
    theme: String = "",
    shuffle: Boolean? = false,
): PlayerSettingsResponse =
    PlayerSettingsResponse(
        mode = mode,
        autoplay = autoplay,
        accentColor = accentColor,
        theme = theme,
        shuffle = shuffle,
    )
