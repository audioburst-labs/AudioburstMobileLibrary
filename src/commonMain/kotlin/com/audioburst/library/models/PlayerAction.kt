package com.audioburst.library.models

data class PlayerAction(
    val type: Type,
    val value: String,
) {

    enum class Type(val id: String) {
        Personalized("p_playlist"), Channel("channel");
    }
}
