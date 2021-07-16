package com.audioburst.library.models

class PlayerSettings(
    val mode: Mode,
    val autoplay: Boolean,
    val accentColor: String,
    val theme: Theme,
    val isShuffleEnabled: Boolean,
) {

    enum class Theme(val themeName: String) {
        Light("light"), Dark("dark");

        companion object {
            internal fun create(themeName: String): Theme = values().firstOrNull { it.themeName == themeName } ?: Light
        }
    }

    enum class Mode(val primaryName: String, val secondaryName: String) {
        Banner(
            primaryName = "banner",
            secondaryName = "mini",
        ),
        Button(
            primaryName = "button",
            secondaryName = "floating",
        );

        companion object {
            internal fun create(modeName: String): Mode = values().firstOrNull {
                it.primaryName == modeName || it.secondaryName == modeName
            } ?: Banner
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlayerSettings

        if (mode != other.mode) return false
        if (autoplay != other.autoplay) return false
        if (accentColor != other.accentColor) return false
        if (theme != other.theme) return false
        if (isShuffleEnabled != other.isShuffleEnabled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode.hashCode()
        result = 31 * result + autoplay.hashCode()
        result = 31 * result + accentColor.hashCode()
        result = 31 * result + theme.hashCode()
        result = 31 * result + isShuffleEnabled.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlayerSettings(mode=$mode, autoplay=$autoplay, accentColor='$accentColor', theme=$theme, isShuffleEnabled=$isShuffleEnabled)"
    }
}