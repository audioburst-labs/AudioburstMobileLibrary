package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.UserExperienceResponse
import com.audioburst.library.models.PlayerAction
import com.audioburst.library.models.PlaylistRequest

internal class ExperiencePlaylistRequestCreator {

    fun create(experience: UserExperienceResponse): PlaylistRequest =
        if (experience.PlayerActionValue == null) {
            experience.createTopStoryPlaylistRequest()
        } else {
            val id = experience.PlayerActionValue
            val name = experience.name
            val options = experience.requestOptions
            when (experience.PlayerAction) {
                PlayerAction.Type.Channel.id -> PlaylistRequest.Channel(
                    id = id.toIntOrNull() ?: TOP_STORY_PLAYLIST_ID,
                    name = name,
                    options = options,
                )
                PlayerAction.Type.UserGenerated.id -> PlaylistRequest.UserGenerated(
                    id = id,
                    name = name,
                    options = options,
                )
                PlayerAction.Type.Source.id -> PlaylistRequest.Source(
                    id = id,
                    name = name,
                    options = options,
                )
                PlayerAction.Type.Account.id -> PlaylistRequest.Account(
                    id = id,
                    name = name,
                    options = options,
                )
                else -> experience.createTopStoryPlaylistRequest()
            }
        }

    private val UserExperienceResponse.requestOptions: PlaylistRequest.Options
        get() = PlaylistRequest.Options(shuffle = playerSettings.shuffle ?: false)

    private fun UserExperienceResponse.createTopStoryPlaylistRequest(): PlaylistRequest =
        PlaylistRequest.Channel(
            id = TOP_STORY_PLAYLIST_ID,
            name = name,
            options = requestOptions,
        )

    companion object {
        private const val TOP_STORY_PLAYLIST_ID = 1
    }
}