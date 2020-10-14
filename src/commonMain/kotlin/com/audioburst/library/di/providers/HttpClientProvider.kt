package com.audioburst.library.di.providers

import com.audioburst.library.utils.SubscriptionKeyGetter
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*

internal class HttpClientProvider(
    private val serializerProvider: Provider<JsonSerializer>,
    private val subscriptionKeyGetterProvider: Provider<SubscriptionKeyGetter>,
) : Singleton<HttpClient>() {

    override fun creator(): HttpClient =
        HttpClient {
            install(JsonFeature) {
                serializer = serializerProvider.get()
            }
            defaultRequest {
                headers.append(SUBSCRIPTION_KEY_NAME, subscriptionKeyGetterProvider.get().subscriptionKey.value)
            }
        }

    companion object {
        private const val SUBSCRIPTION_KEY_NAME = "Ocp-Apim-Subscription-Key"
    }
}
