package com.audioburst.library.utils

import com.audioburst.library.models.*

internal interface LibraryConfiguration {
    val sessionId: SessionId
    val libraryKey: LibraryKey
    val libraryVersion: LibraryVersion
    val subscriptionKey: SubscriptionKey
}

internal interface SubscriptionKeySetter {
    fun set(subscriptionKey: SubscriptionKey)
}

internal object LibraryConfigurationHolder: LibraryConfiguration, SubscriptionKeySetter {

    override val sessionId: SessionId = SessionId(UuidFactory.getUuid())
    override val libraryKey: LibraryKey = platformLibraryKey
    private const val _libraryVersion = "0.0.1"
    override val libraryVersion: LibraryVersion = LibraryVersion(_libraryVersion)

    private var _subscriptionKey: SubscriptionKey? = null
    override val subscriptionKey: SubscriptionKey
        get() = _subscriptionKey ?: throw IllegalStateException(
            "Accessing Subscription Key before it was set by the user."
        )

    override fun set(subscriptionKey: SubscriptionKey) {
        _subscriptionKey = subscriptionKey
    }
}
