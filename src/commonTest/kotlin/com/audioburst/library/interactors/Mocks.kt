package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.PersonalPlaylistRepository
import com.audioburst.library.data.repository.PlaylistRepository
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.libraryConfigurationOf
import com.audioburst.library.data.repository.mappers.playerActionOf
import com.audioburst.library.data.repository.mappers.userPreferenceOf
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.ListenedBurstStorage
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration
import com.audioburst.library.utils.TimestampProvider
import com.audioburst.library.utils.UuidFactory
import com.audioburst.library.utils.timestampProviderOf

internal fun resourceErrorOf(errorType: ErrorType = ErrorType.UnexpectedException(Exception())): Resource.Error =
    Resource.Error(errorType)

internal fun userOf(userId: String = ""): User = User(userId = userId)

internal fun playlistOf(
    id: String = "",
    name: String = "",
    query: String = "",
    bursts: List<Burst> = emptyList(),
    playerSessionId: PlayerSessionId = PlayerSessionId(""),
    playerAction: PlayerAction = playerActionOf(),
    intent: Playlist.Intent? = null,
): Playlist =
    Playlist(
        id = id,
        name = name,
        query = query,
        bursts = bursts,
        playerSessionId = playerSessionId,
        playerAction = playerAction,
        intent = intent,
    )

internal fun playlistInfoOf(
    section: String = "",
    id: Int = 0,
    name: String = "",
    description: String = "",
    image: String = "",
    squareImage: String = "",
    url: String = "",
): PlaylistInfo =
    PlaylistInfo(
        section = section,
        id = id,
        name = name,
        description = description,
        image = image,
        squareImage = squareImage,
        url = url,
    )

internal fun promoteDataOf(
    advertisement: Advertisement? = null,
    ctaData: CtaData? = null,
): PromoteData =
    PromoteData(
        advertisement = advertisement,
        ctaData = ctaData,
    )

internal fun advertisementOf(
    burstUrl: String? = null,
    id: String = "",
    type: String = "",
    audioURL: String = "",
    duration: Duration = 0.0.toDuration(DurationUnit.Milliseconds),
    pixelURL: String = "",
    position: String = "",
    provider: String = "",
    reportingData: List<ReportingData> = emptyList(),
): Advertisement =
    Advertisement(
        burstUrl = burstUrl,
        id = id,
        type = type,
        audioURL = audioURL,
        duration = duration,
        pixelURL = pixelURL,
        position = position,
        provider = provider,
        reportingData = reportingData,
    )

internal fun reportingDataOf(
    url: String = "",
    text: String = "",
    position: Double = 0.0,
): ReportingData = ReportingData(
    url = url,
    text = text,
    position = position,
)

internal fun downloadedAdvertisementOf(
    downloadUrl: Url = Url(""),
    advertisement: Advertisement = advertisementOf(),
): DownloadedAdvertisement =
    DownloadedAdvertisement(
        downloadUrl = downloadUrl,
        advertisement = advertisement,
    )

internal fun burstOf(
    id: String = "",
    title: String = "",
    creationDate: String = "",
    duration: Duration = 0.0.toDuration(DurationUnit.Milliseconds),
    sourceName: String = "",
    category: String? = null,
    playlistId: Long = 0L,
    showName: String = "",
    streamUrl: String? = null,
    audioUrl: String = "",
    imageUrls: List<String> = emptyList(),
    source: BurstSource = burstSourceOf(),
    shareUrl: String = "",
    keywords: List<String> = emptyList(),
    ctaData: CtaData? = null,
    adUrl: String? = null,
): Burst =
    Burst(
        id = id,
        title = title,
        creationDate = creationDate,
        duration = duration,
        sourceName = sourceName,
        category = category,
        playlistId = playlistId,
        showName = showName,
        streamUrl = streamUrl,
        audioUrl = audioUrl,
        imageUrls = imageUrls,
        source = source,
        shareUrl = shareUrl,
        keywords = keywords,
        ctaData = ctaData,
        adUrl = adUrl,
    )

internal fun ctaDataOf(
    buttonText: String = "",
    url: String = "",
): CtaData =
    CtaData(
        buttonText = buttonText,
        url = url,
    )

internal fun burstSourceOf(
    sourceName: String = "",
    sourceType: String? = null,
    showName: String = "",
    durationFromStart: Duration = 0.0.toDuration(DurationUnit.Milliseconds),
    audioUrl: String? = null,
): BurstSource =
    BurstSource(
        sourceName = sourceName,
        sourceType = sourceType,
        showName = showName,
        durationFromStart = durationFromStart,
        audioUrl = audioUrl,
    )

internal fun eventPayloadOf(
    playerAction: PlayerAction = playerActionOf(),
    playlistId: String = "",
    playlistName: String = "",
    burst: Burst = burstOf(),
    isPlaying: Boolean = false,
    occurrenceTime: Long = 0,
    currentPlayBackPosition: Duration = 0.0.toDuration(DurationUnit.Seconds),
    playerSessionId: PlayerSessionId = PlayerSessionId(value = ""),
    advertisement: Advertisement? = null,
    currentPlaybackUrl: String? = null,
): EventPayload = EventPayload(
    playerAction = playerAction,
    playlistId = playlistId,
    playlistName = playlistName,
    burst = burst,
    isPlaying = isPlaying,
    occurrenceTime = occurrenceTime.toDouble().toDuration(DurationUnit.Milliseconds),
    currentPlayBackPosition = currentPlayBackPosition,
    playerSessionId = playerSessionId,
    advertisement = advertisement,
    currentPlaybackUrl = currentPlaybackUrl,
)

internal fun getUserOf(resource: Resource<User>): GetUser =
    object : GetUser {
        override suspend fun invoke(): Resource<User> = resource
    }

internal fun playlistStorageOf(currentPlaylist: Playlist? = null, currentAds: List<DownloadedAdvertisement> = emptyList()): PlaylistStorage =
    object : PlaylistStorage {
        override val currentPlaylist: Playlist? = currentPlaylist
        override val currentAds: List<DownloadedAdvertisement> = currentAds
        override fun setPlaylist(playlist: Playlist) = Unit
        override fun setAdvertisement(url: Url, advertisement: Advertisement) = Unit
    }

internal fun userRepositoryOf(
    returns: MockUserRepository.Returns = MockUserRepository.Returns(),
    sentEvents: MutableList<MockUserRepository.SentEvent> = mutableListOf()
): UserRepository =
    MockUserRepository(
        returns = returns,
        sentEvents = sentEvents
    )

internal fun playlistRepositoryOf(
    returns: MockPlaylistRepository.Returns = MockPlaylistRepository.Returns(),
): PlaylistRepository =
    MockPlaylistRepository(
        returns = returns,
    )

internal class MockUserRepository(
    private val returns: Returns,
    private val sentEvents: MutableList<SentEvent>,
) : UserRepository {

    override suspend fun registerUser(userId: String): Resource<User> = returns.registerUser

    override suspend fun verifyUserId(userId: String): Resource<User> = returns.verifyUserId

    override suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>> = returns.getPlaylists

    override suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit> = returns.postPlayerEvent.apply {
        sentEvents.add(SentEvent(name, playerEvent))
    }

    override suspend fun postReportingData(reportingData: ReportingData): Resource<Unit> = returns.postReportingData

    override suspend fun postBurstPlayback(playlistId: Long, burstId: String, userId: String): Resource<Unit> = returns.postBurstPlayback

    override suspend fun getPromoteData(adUrl: Url): Resource<PromoteData> = returns.getPromoteData

    override suspend fun getUserExperience(applicationKey: String, experienceId: String): Resource<UserExperience> = returns.getUserExperience

    override suspend fun getBurstShareUrl(burst: Burst): Resource<BurstShareUrl> = returns.getBurstShareUrl

    data class Returns(
        val registerUser: Resource<User> = Resource.Data(userOf()),
        val verifyUserId: Resource<User> = Resource.Data(userOf()),
        val getPlaylists: Resource<List<PlaylistInfo>> = Resource.Data(listOf()),
        val postPlayerEvent: Resource<Unit> = Resource.Data(Unit),
        val postReportingData: Resource<Unit> = Resource.Data(Unit),
        val postBurstPlayback: Resource<Unit> = Resource.Data(Unit),
        val getPromoteData: Resource<PromoteData> = Resource.Data(promoteDataOf()),
        val getUserExperience: Resource<UserExperience> = Resource.Data(userExperienceOf()),
        val getBurstShareUrl: Resource<BurstShareUrl> = Resource.Data(burstShareUrlOf()),
    )

    data class SentEvent(
        val name: String,
        val playerEvent: PlayerEvent,
    )
}

internal class MockPlaylistRepository(private val returns: Returns) : PlaylistRepository {
    override suspend fun channel(playlistRequest: PlaylistRequest.Channel, userId: String): Resource<Playlist> = returns.channel

    override suspend fun userGenerated(playlistRequest: PlaylistRequest.UserGenerated, userId: String): Resource<Playlist> = returns.userGenerated

    override suspend fun source(playlistRequest: PlaylistRequest.Source, userId: String): Resource<Playlist> = returns.source

    override suspend fun account(playlistRequest: PlaylistRequest.Account, userId: String): Resource<Playlist> = returns.account

    override suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist> = returns.getPlaylistByPlaylistInfo

    override suspend fun getPlaylist(userId: String, byteArray: ByteArray): Resource<Playlist> = returns.getPlaylistByByteArray

    override suspend fun search(userId: String, query: String): Resource<Playlist> = returns.search

    override fun url(userId: String, playerAction: PlayerAction): Url? = returns.url

    data class Returns(
        val userGenerated: Resource<Playlist> = Resource.Data(playlistOf()),
        val channel: Resource<Playlist> = Resource.Data(playlistOf()),
        val source: Resource<Playlist> = Resource.Data(playlistOf()),
        val account: Resource<Playlist> = Resource.Data(playlistOf()),
        val getPlaylistByPlaylistInfo: Resource<Playlist> = Resource.Data(playlistOf()),
        val getPlaylistByByteArray: Resource<Playlist> = Resource.Data(playlistOf()),
        val search: Resource<Playlist> = Resource.Data(playlistOf()),
        val url: Url? = null
    )
}

internal fun playbackEventHandlerInteractorOf(
    userStorage: UserStorage = userStorageOf(),
    userRepository: UserRepository = userRepositoryOf(),
    unsentEventStorage: UnsentEventStorage = InMemoryUnsentEventStorage(),
    libraryConfiguration: LibraryConfiguration = libraryConfigurationOf(),
    listenedBurstStorage: ListenedBurstStorage = InMemoryListenedBurstStorage(),
    timestampProvider: TimestampProvider = timestampProviderOf(),
): PlaybackEventHandlerInteractor =
    PlaybackEventHandlerInteractor(
        userStorage = userStorage,
        userRepository = userRepository,
        unsentEventStorage = unsentEventStorage,
        libraryConfiguration = libraryConfiguration,
        listenedBurstStorage = listenedBurstStorage,
        timestampProvider = timestampProvider,
    )

internal class InMemoryListenedBurstStorage(private val listenedBurstsExpireDays: Long = 30) : ListenedBurstStorage {
    private val list = mutableListOf<ListenedBurst>()

    override suspend fun getRecentlyListened(): List<ListenedBurst> = list.toList()

    override suspend fun addOrUpdate(listenedBurst: ListenedBurst) {
        list.add(listenedBurst)
    }

    override suspend fun removeExpiredListenedBursts() {
        val expirationDate = DateTime.now().minusDays(listenedBurstsExpireDays)
        list.filter { it.isExpired(expirationDate) }
            .forEach(list::remove)
    }

    private fun ListenedBurst.isExpired(expirationDate: DateTime): Boolean = dateTime.isBefore(expirationDate)

    fun clear() {
        list.clear()
    }
}

internal class MemorablePlaybackEventHandler : PlaybackEventHandler {
    val sentEvents: MutableList<Event> = mutableListOf()
    override suspend fun handle(event: Event) {
        sentEvents.add(event)
    }
}

internal class InMemoryUnsentEventStorage : UnsentEventStorage {
    private val playerEvents = mutableListOf<PlayerEvent>()
    override suspend fun getAllPlayerEvents(): List<PlayerEvent> = playerEvents
    override suspend fun add(playerEvent: PlayerEvent) { playerEvents.add(playerEvent) }
    override suspend fun remove(playerEvent: PlayerEvent) { playerEvents.remove(playerEvent) }
}

internal fun uuidFactoryOf(uuid: String = ""): UuidFactory =
    UuidFactory { uuid }

internal fun personalPlaylistQueryIdOf(queryId: Long = 0L) = PersonalPlaylistQueryId(queryId)

internal fun pendingPlaylistOf(
    isReady: Boolean = false,
    playlist: Playlist = playlistOf(),
): PendingPlaylist =
    PendingPlaylist(
        isReady = isReady,
        playlist = playlist,
    )

internal fun personalPlaylistRepositoryOf(
    returns: MockPersonalPlaylistRepository.Returns = MockPersonalPlaylistRepository.Returns()
): PersonalPlaylistRepository =
    MockPersonalPlaylistRepository(returns = returns)

internal class MockPersonalPlaylistRepository(private val returns: Returns) : PersonalPlaylistRepository {

    override suspend fun getUserPreferences(user: User): Resource<UserPreferences> = returns.getUserPreferences

    override suspend fun postUserPreferences(user: User, userPreferences: UserPreferences): Resource<UserPreferences> = returns.postUserPreferences

    override suspend fun getPersonalPlaylistQueryId(user: User): Resource<PersonalPlaylistQueryId> = returns.getPersonalPlaylistQueryId

    private var callNumber = 0
    override suspend fun getPersonalPlaylist(user: User, personalPlaylistQueryId: PersonalPlaylistQueryId): Resource<PendingPlaylist> =
        returns.getPersonalPlaylist[callNumber].apply { callNumber++ }

    data class Returns(
        val getUserPreferences: Resource<UserPreferences> = Resource.Data(userPreferenceOf()),
        val postUserPreferences: Resource<UserPreferences> = Resource.Data(userPreferenceOf()),
        val getPersonalPlaylistQueryId: Resource<PersonalPlaylistQueryId> = Resource.Data(personalPlaylistQueryIdOf()),
        val getPersonalPlaylist: List<Resource<PendingPlaylist>> = listOf(Resource.Data(pendingPlaylistOf()))
    )
}

internal fun postContentLoadEventOf(
    playbackEventHandler: PlaybackEventHandler = playbackEventHandlerInteractorOf(),
    timestampProvider: TimestampProvider = timestampProviderOf(),
) : PostContentLoadEvent =
    PostContentLoadEvent(
        playbackEventHandler = playbackEventHandler,
        timestampProvider = timestampProvider,
    )

internal fun listenedBurstOf(id: String = "", dateTime: DateTime = DateTime.now()): ListenedBurst =
    ListenedBurst(id = id, dateTime = dateTime)

internal fun listenedBurstsStorageOf(
    getAll: List<ListenedBurst> = emptyList(),
): ListenedBurstStorage =
    object : ListenedBurstStorage {
        override suspend fun getRecentlyListened(): List<ListenedBurst> = getAll
        override suspend fun addOrUpdate(listenedBurst: ListenedBurst) = Unit
        override suspend fun removeExpiredListenedBursts() = Unit
    }

internal fun userExperienceOf(
    id: String = "",
    playerSettings: PlayerSettings = playerSettingsOf(),
    request: PlaylistRequest = sourceRequestOf(),
): UserExperience =
    UserExperience(
        id = id,
        playerSettings = playerSettings,
        request = request,
    )

internal fun playerSettingsOf(
    mode: PlayerSettings.Mode = PlayerSettings.Mode.Banner,
    autoplay: Boolean = false,
    accentColor: String = "",
    theme: PlayerSettings.Theme = PlayerSettings.Theme.Light,
    isShuffleEnabled: Boolean = false,
): PlayerSettings =
    PlayerSettings(
        mode = mode,
        autoplay = autoplay,
        accentColor = accentColor,
        theme = theme,
        isShuffleEnabled = isShuffleEnabled,
    )

internal fun userGeneratedRequestOf(
    id: String = "",
    name: String = "",
    options: PlaylistRequest.Options = requestOptionsOf(),
): PlaylistRequest.UserGenerated =
    PlaylistRequest.UserGenerated(
        id = id,
        name = name,
        options = options,
    )

internal fun sourceRequestOf(
    id: String = "",
    name: String = "",
    options: PlaylistRequest.Options = requestOptionsOf(),
): PlaylistRequest.Source =
    PlaylistRequest.Source(
        id = id,
        name = name,
        options = options,
    )

internal fun accountRequestOf(
    id: String = "",
    name: String = "",
    options: PlaylistRequest.Options = requestOptionsOf(),
): PlaylistRequest.Account =
    PlaylistRequest.Account(
        id = id,
        name = name,
        options = options,
    )

internal fun channelRequestOf(
    id: Int = 0,
    name: String = "",
    options: PlaylistRequest.Options = requestOptionsOf(),
): PlaylistRequest.Channel =
    PlaylistRequest.Channel(
        id = id,
        name = name,
        options = options,
    )

internal fun requestOptionsOf(
    firstBurstId: String? = null,
    shuffle: Boolean = false,
): PlaylistRequest.Options =
    PlaylistRequest.Options(
        firstBurstId = firstBurstId,
        shuffle = shuffle,
    )

internal fun burstShareUrlOf(shortUrl: String = ""): BurstShareUrl = BurstShareUrl(shortUrl)
