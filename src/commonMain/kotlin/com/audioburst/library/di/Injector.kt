package com.audioburst.library.di

import com.audioburst.library.AudioburstLibraryDelegate
import com.audioburst.library.data.remote.AbAiRouterApi
import com.audioburst.library.data.remote.AudioburstApi
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.HttpPersonalPlaylistRepository
import com.audioburst.library.data.repository.HttpUserRepository
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.*
import com.audioburst.library.data.storage.*
import com.audioburst.library.di.providers.*
import com.audioburst.library.interactors.*
import com.audioburst.library.models.AppDispatchers
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration
import com.audioburst.library.utils.*
import com.audioburst.library.utils.strategies.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

internal object Injector {

    private val playbackStateCheckInterval = 2.0.toDuration(DurationUnit.Seconds)
    private const val SETTINGS_NAME = "com.audioburst.library"

    private val jsonProvider: Provider<Json> = JsonProvider()
    private val serializerProvider: Provider<JsonSerializer> = provider { KotlinxSerializer(json = jsonProvider.get()) }
    private val libraryConfigurationProvider: Provider<LibraryConfiguration> = singleton {
        LibraryConfigurationHolder(
            uuidFactory = uuidFactoryProvider.get()
        )
    }
    private val httpClientProvider: Provider<HttpClient> = HttpClientProvider(
        serializerProvider = serializerProvider,
        libraryConfigurationProvider = libraryConfigurationProvider,
    )
    private val audioburstV2ApiProvider: Provider<AudioburstV2Api> = provider { AudioburstV2Api(jsonProvider.get()) }
    private val audioburstApiProvider: Provider<AudioburstApi> = provider { AudioburstApi() }
    private val userResponseToUserProvider: Provider<UserResponseToUserMapper> = provider { UserResponseToUserMapper() }
    private val settingsProvider: Provider<Settings> = singleton { createSettings(SETTINGS_NAME) }
    private val userStorageProvider: Provider<UserStorage> = provider {
        SettingsUserStorage(
            settings = settingsProvider.get(),
            settingsName = SETTINGS_NAME,
        )
    }
    private val playlistResponseToPlaylistInfoProvider: Provider<PlaylistResponseToPlaylistInfoMapper> = provider { PlaylistResponseToPlaylistInfoMapper() }
    private val uuidFactoryProvider: Provider<UuidFactory> = singleton { PlatformUuidFactory() }
    private val playerSessionIdGetterProvider: Provider<PlayerSessionIdGetter> = provider {
        UuidBasedPlayerSessionIdGetter(
            uuidFactory = uuidFactoryProvider.get()
        )
    }
    private val advertisementResponseToAdvertisementMapperProvider: Provider<AdvertisementResponseToAdvertisementMapper> = provider { AdvertisementResponseToAdvertisementMapper() }
    private val sourceResponseToBurstSourceProvider: Provider<SourceResponseToBurstSourceMapper> = provider { SourceResponseToBurstSourceMapper() }
    private val burstResponseToBurstProvider: Provider<BurstResponseToBurstMapper> = provider {
        BurstResponseToBurstMapper(
            libraryConfiguration = libraryConfigurationProvider.get(),
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
    private val playerEventToEventRequestProvider: Provider<PlayerEventToEventRequestMapper> = provider {
        PlayerEventToEventRequestMapper(
            json = jsonProvider.get(),
            advertisementEventToAdvertisementEventRequestMapper = advertisementEventToAdvertisementEventRequestProvider.get(),
        )
    }
    private val playlistStorageProvider: Provider<PlaylistStorage> = singleton { InMemoryPlaylistStorage() }
    private val userRepositoryProvider: Provider<UserRepository> = provider {
        HttpUserRepository(
            httpClient = httpClientProvider.get(),
            audioburstV2Api = audioburstV2ApiProvider.get(),
            abAiRouterApi = abAiRouterApiProvider.get(),
            audioburstApi = audioburstApiProvider.get(),
            libraryConfiguration = libraryConfigurationProvider.get(),
            userResponseToUserMapper = userResponseToUserProvider.get(),
            playlistResponseToPlaylistInfoMapper = playlistResponseToPlaylistInfoProvider.get(),
            topStoryResponseToPlaylist = topStoryResponseToPlaylistProvider.get(),
            advertisementResponseToAdvertisementMapper = advertisementResponseToAdvertisementMapperProvider.get(),
            playerEventToEventRequestMapper = playerEventToEventRequestProvider.get(),
            playlistStorage = playlistStorageProvider.get(),
        )
    }
    private val currentAdsProvider: Provider<CurrentAdsProvider> = provider {
        CurrentAdsInteractor(
            playlistStorage = playlistStorageProvider.get()
        )
    }
    private val currentPlaylistProvider: Provider<CurrentPlaylist> = provider {
        CurrentPlaylistInteractor(
            playlistStorage = playlistStorageProvider.get()
        )
    }
    private val unsentEventStorageProvider: Provider<UnsentEventStorage> = provider { NoOpUnsentEventStorage() }
    private val playbackEventHandlerProvider: Provider<PlaybackEventHandler> = provider {
        PlaybackEventHandlerInteractor(
            userStorage = userStorageProvider.get(),
            userRepository = userRepositoryProvider.get(),
            unsentEventStorage = unsentEventStorageProvider.get(),
            libraryConfiguration = libraryConfigurationProvider.get(),
        )
    }
    private val timestampProviderProvider: Provider<TimestampProvider> = provider { PlatformTimestampProvider }
    private val listenedMediaStrategyFactoryProvider: Provider<ListenedMediaStrategy.Factory> = provider {
        ListenedMediaStrategy.Factory(
            refreshInterval = playbackStateCheckInterval
        )
    }
    private val playbackPeriodsCreatorProvider: Provider<PlaybackPeriodsCreator> = provider { InputBasedPlaybackPeriodsCreator() }
    private val listenedStrategyProvider: Provider<ListenedStrategy> = provider {
        ListenedStrategy(
            factory = listenedMediaStrategyFactoryProvider.get(),
            creator = playbackPeriodsCreatorProvider.get(),
        )
    }
    private val strategiesProvider: Provider<List<PlaybackEventStrategy<*>>> = provider {
        listOf(
            AdListenedStrategy(), Back30SecStrategy(), BackStrategy(), BackToBurstStrategy(), EndOfPlayStrategy(),
            EndOfPlaylistStrategy(), ForwardStrategy(), KeepListeningStrategy(), RepeatStrategy(), RewindStrategy(),
            Skip30SecStrategy(), SkipStrategy(), StartOfPlayStrategy(),
        )
    }
    private val subscriptionKeySetterProvider: Provider<SubscriptionKeySetter> = provider { libraryConfigurationProvider.get() as SubscriptionKeySetter }
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
    private val postContentLoadEventProvider: Provider<PostContentLoadEvent> = provider {
        PostContentLoadEvent(
            playbackEventHandler = playbackEventHandlerProvider.get(),
            timestampProvider = timestampProviderProvider.get(),
        )
    }
    private val getPlaylistProvider: Provider<GetPlaylist> = provider {
        GetPlaylist(
            getUser = getUserProvider.get(),
            userRepository = userRepositoryProvider.get(),
            postContentLoadEvent = postContentLoadEventProvider.get(),
        )
    }
    private val getAdDataProvider: Provider<GetAdUrl> = provider {
        GetAdUrl(
            userRepository = userRepositoryProvider.get(),
        )
    }
    private val appDispatchersProvider: Provider<AppDispatchers> = provider {
        AppDispatchers(
            io = Dispatchers.Default,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }
    private val eventDetectorProvider: Provider<StrategyBasedEventDetector> = singleton {
        StrategyBasedEventDetector(
            currentPlaylist = currentPlaylistProvider.get(),
            currentAds = currentAdsProvider.get(),
            playbackEventHandler = playbackEventHandlerProvider.get(),
            strategies = strategiesProvider.get(),
            timestampProvider = timestampProviderProvider.get(),
            appDispatchers = appDispatchersProvider.get(),
            checkInterval = playbackStateCheckInterval,
            listenedStrategy = listenedStrategyProvider.get(),
        )
    }

    private val userPreferenceResponseToPreferenceMapperProvider: Provider<UserPreferenceResponseToPreferenceMapper> = provider { UserPreferenceResponseToPreferenceMapper() }

    private val preferenceToUserPreferenceResponseMapperProvider: Provider<PreferenceToUserPreferenceResponseMapper> = provider { PreferenceToUserPreferenceResponseMapper() }

    private val topStoryResponseToPendingPlaylistProvider: Provider<TopStoryResponseToPendingPlaylist> = provider {
        TopStoryResponseToPendingPlaylist(
            topStoryResponseToPlaylist = topStoryResponseToPlaylistProvider.get()
        )
    }

    private val personalPlaylistRepositoryProvider: Provider<PersonalPlaylistRepository> = provider {
        HttpPersonalPlaylistRepository(
            httpClient = httpClientProvider.get(),
            audioburstV2Api = audioburstV2ApiProvider.get(),
            userPreferenceResponseToPreferenceMapper = userPreferenceResponseToPreferenceMapperProvider.get(),
            preferenceToUserPreferenceResponseMapper = preferenceToUserPreferenceResponseMapperProvider.get(),
            topStoryResponseToPendingPlaylist = topStoryResponseToPendingPlaylistProvider.get(),
        )
    }

    private val getUserPreferencesProvider: Provider<GetUserPreferences> = provider {
        GetUserPreferences(
            getUser = getUserProvider.get(),
            personalPlaylistRepository = personalPlaylistRepositoryProvider.get(),
        )
    }

    private val postUserPreferencesProvider: Provider<PostUserPreferences> = provider {
        PostUserPreferences(
            getUser = getUserProvider.get(),
            personalPlaylistRepository = personalPlaylistRepositoryProvider.get(),
        )
    }

    private val observePersonalPlaylistProvider: Provider<ObservePersonalPlaylist> = provider {
        ObservePersonalPlaylist(
            getUser = getUserProvider.get(),
            personalPlaylistRepository = personalPlaylistRepositoryProvider.get(),
            postContentLoadEvent = postContentLoadEventProvider.get(),
        )
    }

    private val updateUserIdProvider: Provider<UpdateUserId> = provider {
        UpdateUserId(
            userStorage = userStorageProvider.get(),
            userRepository = userRepositoryProvider.get()
        )
    }

    fun inject(audioburstLibrary: AudioburstLibraryDelegate) {
        with(audioburstLibrary) {
            observePersonalPlaylist = observePersonalPlaylistProvider.get()
            subscriptionKeySetter = subscriptionKeySetterProvider.get()
            postUserPreferences = postUserPreferencesProvider.get()
            getUserPreferences = getUserPreferencesProvider.get()
            getPlaylistsInfo = getPlaylistsInfoProvider.get()
            appDispatchers = appDispatchersProvider.get()
            eventDetector = eventDetectorProvider.get()
            updateUserId = updateUserIdProvider.get()
            getPlaylist = getPlaylistProvider.get()
            getAdUrl = getAdDataProvider.get()
        }
    }
}
