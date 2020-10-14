package com.audioburst.library.utils

import com.audioburst.library.models.SubscriptionKey

internal interface SubscriptionKeyGetter {
    val subscriptionKey: SubscriptionKey
}

internal interface SubscriptionKeySetter {
    fun set(subscriptionKey: SubscriptionKey)
}

internal object SubscriptionKeyHolder: SubscriptionKeyGetter, SubscriptionKeySetter {

    private var _subscriptionKey: SubscriptionKey? = null

    override val subscriptionKey: SubscriptionKey
        get() = _subscriptionKey ?: throw IllegalStateException(
            "Accessing Subscription Key before it was set by the user."
        )

    override fun set(subscriptionKey: SubscriptionKey) {
        _subscriptionKey = subscriptionKey
    }
}
