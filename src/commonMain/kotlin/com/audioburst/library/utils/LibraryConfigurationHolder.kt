package com.audioburst.library.utils

import co.touchlab.stately.concurrency.AtomicReference
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

internal class LibraryConfigurationHolder(
    uuidFactory: UuidFactory
): LibraryConfiguration, SubscriptionKeySetter {

    override val sessionId: SessionId = SessionId(uuidFactory.getUuid())
    override val libraryKey: LibraryKey = platformLibraryKey
    override val libraryVersion: LibraryVersion = LibraryVersion(LIBRARY_VERSION)

    private var _subscriptionKey: AtomicReference<SubscriptionKey?> = AtomicReference(null)
    override val subscriptionKey: SubscriptionKey
        get() = _subscriptionKey.get() ?: throw IllegalStateException(
            "Accessing Subscription Key before it was set by the user."
        )

    override fun set(subscriptionKey: SubscriptionKey) {
        _subscriptionKey.set(subscriptionKey)
    }

    companion object {
        private const val LIBRARY_VERSION = "0.0.14"
    }
}
