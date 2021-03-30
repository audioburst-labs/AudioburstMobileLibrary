package com.audioburst.library.models

class Playlist(
    val id: String,
    val name: String,
    val query: String,
    val bursts: List<Burst>,
    val playerSessionId: PlayerSessionId,
    internal val playerAction: PlayerAction,
) {

    internal fun copy(bursts: List<Burst>): Playlist =
        Playlist(
            id = id,
            name = name,
            query = query,
            bursts = bursts,
            playerSessionId = playerSessionId,
            playerAction = playerAction
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Playlist

        if (id != other.id) return false
        if (name != other.name) return false
        if (query != other.query) return false
        if (bursts != other.bursts) return false
        if (playerSessionId != other.playerSessionId) return false
        if (playerAction != other.playerAction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + query.hashCode()
        result = 31 * result + bursts.hashCode()
        result = 31 * result + playerSessionId.hashCode()
        result = 31 * result + playerAction.hashCode()
        return result
    }

    override fun toString(): String {
        return "Playlist(id='$id', name='$name', query='$query', bursts=$bursts, playerSessionId=$playerSessionId, playerAction=$playerAction)"
    }
}
