package com.audioburst.library.models

class PlayerAction(
    val type: Type,
    val value: String,
) {

    enum class Type(val id: String) {
        Personalized("p_playlist"), Channel("channel"), Voice("Voice"), Search("search");
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlayerAction

        if (type != other.type) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlayerAction(type=$type, value='$value')"
    }
}
