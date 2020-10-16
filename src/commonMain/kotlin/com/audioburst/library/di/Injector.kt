package com.audioburst.library.di

import com.audioburst.library.AudioburstLibrary
import com.audioburst.library.data.remote.AbAiRouterApi
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.HttpUserRepository
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.*
import com.audioburst.library.data.storage.SettingsUserStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.data.storage.settings
import com.audioburst.library.di.providers.*
import com.audioburst.library.interactors.*
import com.audioburst.library.utils.*
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

internal object Injector {

    private val jsonProvider: Provider<Json> = JsonProvider()
    private val serializerProvider: Provider<JsonSerializer> = provider { KotlinxSerializer(json = jsonProvider.get()) }
    private val subscriptionKeyGetterProvider: Provider<SubscriptionKeyGetter> = provider { SubscriptionKeyHolder }
    private val httpClientProvider: Provider<HttpClient> = HttpClientProvider(
        serializerProvider = serializerProvider,
        subscriptionKeyGetterProvider = subscriptionKeyGetterProvider,
    )
    private val audioburstV2ApiProvider: Provider<AudioburstV2Api> = provider { AudioburstV2Api() }
    private val registerResponseToUserProvider: Provider<RegisterResponseToUserMapper> = provider { RegisterResponseToUserMapper() }
    private val settingsProvider: Provider<Settings> = singleton { settings() }
    private val userStorageProvider: Provider<UserStorage> = provider {
        SettingsUserStorage(
            settings = settingsProvider.get()
        )
    }
    private val playlistResponseToPlaylistInfoProvider: Provider<PlaylistResponseToPlaylistInfoMapper> = provider { PlaylistResponseToPlaylistInfoMapper() }
    private val uuidFactoryProvider: Provider<UuidFactory> = provider { UuidFactory }
    private val playerSessionIdGetterProvider: Provider<PlayerSessionIdGetter> = provider {
        UuidBasedPlayerSessionIdGetter(
            uuidFactory = uuidFactoryProvider.get()
        )
    }
    private val promoteResponseToAdvertisementProvider: Provider<PromoteResponseToAdvertisementMapper> = provider { PromoteResponseToAdvertisementMapper() }
    private val sourceResponseToBurstSourceProvider: Provider<SourceResponseToBurstSourceMapper> = provider { SourceResponseToBurstSourceMapper() }
    private val burstResponseToBurstProvider: Provider<BurstResponseToBurstMapper> = provider {
        BurstResponseToBurstMapper(
            subscriptionKeyGetter = subscriptionKeyGetterProvider.get(),
            sourceResponseToBurstSourceMapper = sourceResponseToBurstSourceProvider.get(),
        )
    }
    private val topStoryResponseToPlaylistProvider: Provider<TopStoryResponseToPlaylist> = provider {
        TopStoryResponseToPlaylist(
            playerSessionIdGetter = playerSessionIdGetterProvider.get(),
            burstResponseToBurstMapper = burstResponseToBurstProvider.get(),
        )
    }
    private val abAiRouterApiProvider: Provider<AbAiRouterApi> = provider { AbAiRouterApi() }
    private val advertisementEventToAdvertisementEventRequestProvider: Provider<AdvertisementEventToAdvertisementEventRequestMapper> = provider { AdvertisementEventToAdvertisementEventRequestMapper() }
    private val jsonEncoderProvider: Provider<JsonEncoder> = provider { SerializationJsonEncoder(json = jsonProvider.get()) }
    private val playerEventToEventRequestProvider: Provider<PlayerEventToEventRequestMapper> = provider {
        PlayerEventToEventRequestMapper(
            jsonEncoder = jsonEncoderProvider.get()
        )
    }
    private val userRepositoryProvider: Provider<UserRepository> = provider {
        HttpUserRepository(
            httpClient = httpClientProvider.get(),
            audioburstV2Api = audioburstV2ApiProvider.get(),
            abAiRouterApi = abAiRouterApiProvider.get(),
            registerResponseToUserMapper = registerResponseToUserProvider.get(),
            playlistResponseToPlaylistInfoMapper = playlistResponseToPlaylistInfoProvider.get(),
            topStoryResponseToPlaylist = topStoryResponseToPlaylistProvider.get(),
            promoteResponseToAdvertisementMapper = promoteResponseToAdvertisementProvider.get(),
            advertisementEventToAdvertisementEventRequestMapper = advertisementEventToAdvertisementEventRequestProvider.get(),
            playerEventToEventRequestMapper = playerEventToEventRequestProvider.get(),
        )
    }
    private val subscriptionKeySetterProvider: Provider<SubscriptionKeySetter> = provider { SubscriptionKeyHolder }
    private val getUserProvider: Provider<GetUser> = provider {
        GetUserInteractor(
            uuidFactory = uuidFactoryProvider.get(),
            userStorage = userStorageProvider.get(),
            userRepository = userRepositoryProvider.get(),
        )
    }
    private val getPlaylistsInfoProvider: Provider<GetPlaylistsInfo> = provider {
        GetPlaylistsInfo(
            getUser = getUserProvider.get(),
            userRepository = userRepositoryProvider.get(),
        )
    }
    private val getPlaylistProvider: Provider<GetPlaylist> = provider {
        GetPlaylist(
            getUser = getUserProvider.get(),
            userRepository = userRepositoryProvider.get(),
        )
    }
    private val getAdDataProvider: Provider<GetAdData> = provider {
        GetAdData(
            userRepository = userRepositoryProvider.get(),
        )
    }

    fun inject(audioburstLibrary: AudioburstLibrary) {
        with(audioburstLibrary) {
            subscriptionKeySetter = subscriptionKeySetterProvider.get()
            getPlaylistsInfo = getPlaylistsInfoProvider.get()
            getPlaylist = getPlaylistProvider.get()
            getAdData = getAdDataProvider.get()
        }
    }
}
