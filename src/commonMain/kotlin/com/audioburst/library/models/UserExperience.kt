package com.audioburst.library.models

class UserExperience(
    val id: String,
    val playerSettings: PlayerSettings,
    val request: PlaylistRequest,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserExperience

        if (id != other.id) return false
        if (playerSettings != other.playerSettings) return false
        if (request != other.request) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + playerSettings.hashCode()
        result = 31 * result + request.hashCode()
        return result
    }

    override fun toString(): String {
        return "Experience(id='$id', playerSettings=$playerSettings, request=$request)"
    }
}