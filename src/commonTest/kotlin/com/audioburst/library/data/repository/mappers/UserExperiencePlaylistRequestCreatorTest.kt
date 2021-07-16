package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.PlaylistRequest
import kotlin.test.Test
import kotlin.test.assertTrue

private const val TOP_STORY_PLAYLIST_ID = 1

class UserExperiencePlaylistRequestCreatorTest {

    private val creator = ExperiencePlaylistRequestCreator()
    private val id = "id"

    private fun testRequestCreation(
        playerAction: String,
        playerActionValue: String = id,
        name: String,
        requestAssertor: (PlaylistRequest) -> Unit
    ) {
        // GIVEN
        val response = userExperienceResponseOf(
            name = name,
            PlayerAction = playerAction,
            PlayerActionValue = playerActionValue
        )

        // WHEN
        val request = creator.create(response)

        // THEN
        requestAssertor(request)
    }

    @Test
    fun `test if TopStory request is created when PlayerActionValue is equal null`() {
        // GIVEN
        val name = "name"
        val response = userExperienceResponseOf(
            name = name,
            PlayerActionValue = null,
        )

        // WHEN
        val request = creator.create(response)

        // THEN
        require(request is PlaylistRequest.Channel)
        require(request.id == TOP_STORY_PLAYLIST_ID)
        require(request.name == name)
    }

    @Test
    fun `test if Request's shuffle is according to PlayerSettings'shuffle`() {
        // GIVEN
        val response = userExperienceResponseOf(
            PlayerActionValue = null,
            playerSettings = playerSettingsResponseOf(
                shuffle = true
            )
        )

        // WHEN
        val request = creator.create(response)

        // THEN
        assertTrue(request.requestOptions.shuffle)
    }

    @Test
    fun `test if TopStory request is created when PlayerActionValue is equal p_playlist`() {
        val name = "name"
        testRequestCreation(playerAction = "p_playlist", name = name) { request ->
            require(request is PlaylistRequest.Channel)
            require(request.id == TOP_STORY_PLAYLIST_ID)
            require(request.name == name)
        }
    }

    @Test
    fun `test if Channel request is created when PlayerActionValue is equal channel`() {
        val value = 4
        val name = "name"
        testRequestCreation(
            playerAction = "channel",
            playerActionValue = value.toString(),
            name = name,
        ) { request ->
            require(request is PlaylistRequest.Channel)
            require(request.id == value)
            require(request.name == name)
        }
    }

    @Test
    fun `test if Channel request is created when PlayerActionValue is equal anything else`() {
        // GIVEN
        val response = userExperienceResponseOf(
            PlayerActionValue = "null",
        )

        // WHEN
        val request = creator.create(response)

        // THEN
        require(request is PlaylistRequest.Channel)
        require(request.id == TOP_STORY_PLAYLIST_ID)
    }

    @Test
    fun `test if UserGenerated request is created when PlayerActionValue is equal ug_playlist`() {
        val name = "name"
        testRequestCreation(playerAction = "ug_playlist", name = name) { request ->
            require(request is PlaylistRequest.UserGenerated)
            require(request.id == id)
            require(request.name == name)
        }
    }

    @Test
    fun `test if Source request is created when PlayerActionValue is equal s_playlist`() {
        val name = "name"
        testRequestCreation(playerAction = "s_playlist", name = name) { request ->
            require(request is PlaylistRequest.Source)
            require(request.id == id)
            require(request.name == name)
        }
    }

    @Test
    fun `test if Account request is created when PlayerActionValue is equal a_playlist`() {
        val name = "name"
        testRequestCreation(playerAction = "a_playlist", name = name) { request ->
            require(request is PlaylistRequest.Account)
            require(request.id == id)
            require(request.name == name)
        }
    }
}