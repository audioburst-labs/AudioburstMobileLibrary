package com.audioburst.library.utils

import com.audioburst.library.models.*

internal interface LibraryConfiguration {
    val sessionId: SessionId
    val libraryKey: LibraryKey
    val libraryVersion: LibraryVersion
    val subscriptionKey: SubscriptionKey
    val sdkInfo: SdkInfo?
}

internal interface SubscriptionKeySetter {
    fun set(subscriptionKey: SubscriptionKey)
}

internal interface SdkInfoSetter {
    fun set(sdkInfo: SdkInfo)
}

internal class LibraryConfigurationHolder(
    uuidFactory: UuidFactory
): LibraryConfiguration, SubscriptionKeySetter, SdkInfoSetter {

    override val sessionId: SessionId = SessionId(uuidFactory.getUuid())
    override val libraryKey: LibraryKey = platformLibraryKey
    override val libraryVersion: LibraryVersion = LibraryVersion(LIBRARY_VERSION)

    init {
        Logger.i(LIBRARY_VERSION)
    }

    private var _subscriptionKey by nullableAtomic<SubscriptionKey>()
    override val subscriptionKey: SubscriptionKey
        get() = _subscriptionKey ?: throw IllegalStateException(
            "Accessing Subscription Key before it was set by the user."
        )

    private var _sdkInfo by nullableAtomic<SdkInfo>()
    override val sdkInfo: SdkInfo?
        get() = _sdkInfo

    override fun set(subscriptionKey: SubscriptionKey) {
        _subscriptionKey = subscriptionKey
    }

    override fun set(sdkInfo: SdkInfo) {
        _sdkInfo = sdkInfo
    }

    companion object {
        private const val LIBRARY_VERSION = "0.0.29"
    }
}
