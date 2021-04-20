package com.audioburst.library.models

class CtaData(
    val buttonText: String,
    val url: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CtaData

        if (buttonText != other.buttonText) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = buttonText.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

    override fun toString(): String {
        return "CtaData(buttonText='$buttonText', url='$url')"
    }
}