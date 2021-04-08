package com.audioburst.library.models

class PlaylistInfo(
    val section: String,
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val squareImage: String,
    val url: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlaylistInfo

        if (section != other.section) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (image != other.image) return false
        if (squareImage != other.squareImage) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = section.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + squareImage.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlaylistInfo(section='$section', id=$id, name='$name', description='$description', image='$image', squareImage='$squareImage', url='$url')"
    }
}
