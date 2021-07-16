package com.audioburst.library.models

sealed class PlaylistRequest(
    internal val requestOptions: Options,
    internal val playlistName: String,
) {

    internal abstract val playerAction: PlayerAction

    class UserGenerated internal constructor(internal val id: String, internal val name: String, options: Options) : PlaylistRequest(options, name) {
        constructor(id: String) : this(id = id, name = id, options = Options())

        override val playerAction: PlayerAction
            get() = PlayerAction(
                type = PlayerAction.Type.UserGenerated,
                value = id,
            )
    }
    class Source internal constructor(internal val id: String, internal val name: String, options: Options) : PlaylistRequest(options, name) {
        constructor(id: String) : this(id = id, name = id, options = Options())

        override val playerAction: PlayerAction
            get() = PlayerAction(
                type = PlayerAction.Type.Source,
                value = id,
            )
    }
    class Account internal constructor(internal val id: String, internal val name: String, options: Options) : PlaylistRequest(options, name) {
        constructor(id: String) : this(id = id, name = id, options = Options())

        override val playerAction: PlayerAction
            get() = PlayerAction(
                type = PlayerAction.Type.Account,
                value = id,
            )
    }
    class Channel internal constructor(internal val id: Int, internal val name: String, options: Options) : PlaylistRequest(options, name) {
        constructor(id: Int) : this(id = id, name = id.toString(), options = Options())

        override val playerAction: PlayerAction
            get() = PlayerAction(
                type = PlayerAction.Type.Channel,
                value = id.toString(),
            )
    }

    internal data class Options(
        val firstBurstId: String? = null,
        val shuffle: Boolean = false,
    )
}