package com.audioburst.library.interactors

import com.audioburst.library.data.ErrorType
import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.libraryConfigurationOf
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration

internal fun resourceErrorOf(errorType: ErrorType = ErrorType.UnexpectedException(Exception())): Resource.Error =
    Resource.Error(errorType)

internal fun userOf(
    userId: String = "",
    deviceId: String = "",
): User =
    User(
        userId = userId,
        deviceId = deviceId,
    )

internal fun playlistOf(
    id: Int = 0,
    name: String = "",
    query: String = "",
    bursts: List<Burst> = emptyList(),
    playerSessionId: PlayerSessionId = PlayerSessionId(""),
): Playlist =
    Playlist(
        id = id,
        name = name,
        query = query,
        bursts = bursts,
        playerSessionId = playerSessionId,
    )

internal fun playlistInfoOf(
    id: Int = 0,
    name: String = "",
    description: String = "",
    image: String? = null,
    url: String = "",
): PlaylistInfo =
    PlaylistInfo(
        id = id,
        name = name,
        description = description,
        image = image,
        url = url,
    )

internal fun advertisementOf(
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
        adUrl = adUrl,
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
    playlistId: Int = 0,
    playlistName: String = "",
    burst: Burst = burstOf(),
    isPlaying: Boolean = false,
    occurrenceTime: Long = 0,
    currentPlayBackPosition: Duration = 0.0.toDuration(DurationUnit.Seconds),
    playerSessionId: PlayerSessionId = PlayerSessionId(value = ""),
): EventPayload = EventPayload(
    playlistId = playlistId,
    playlistName = playlistName,
    burst = burst,
    isPlaying = isPlaying,
    occurrenceTime = occurrenceTime,
    currentPlayBackPosition = currentPlayBackPosition,
    playerSessionId = playerSessionId,
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
    returns: MockUserRepository.Returns = MockUserRepository.Returns()
): UserRepository =
    MockUserRepository(returns = returns)

internal class MockUserRepository(private val returns: Returns) : UserRepository {

    override suspend fun registerUser(userId: String): Resource<User> = returns.registerUser

    override suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>> = returns.getPlaylists

    override suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist> = returns.getPlaylist

    override suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit> = returns.postPlayerEvent

    override suspend fun postEvent(advertisementEvent: AdvertisementEvent): Resource<Unit> = returns.postAdvertisementEvent

    override suspend fun postReportingData(reportingData: ReportingData): Resource<Unit> = returns.postReportingData

    override suspend fun postBurstPlayback(playlistId: Long, burstId: String, userId: String): Resource<Unit> = returns.postBurstPlayback

    override suspend fun getAdData(adUrl: Url): Resource<Advertisement> = returns.getAdData

    data class Returns(
        val registerUser: Resource<User> = Resource.Data(userOf()),
        val getPlaylists: Resource<List<PlaylistInfo>> = Resource.Data(listOf()),
        val getPlaylist: Resource<Playlist> = Resource.Data(playlistOf()),
        val postPlayerEvent: Resource<Unit> = Resource.Data(Unit),
        val postAdvertisementEvent: Resource<Unit> = Resource.Data(Unit),
        val postReportingData: Resource<Unit> = Resource.Data(Unit),
        val postBurstPlayback: Resource<Unit> = Resource.Data(Unit),
        val getAdData: Resource<Advertisement> = Resource.Data(advertisementOf()),
    )
}

internal fun playbackEventHandlerInteractorOf(
    userStorage: UserStorage = userStorageOf(),
    userRepository: UserRepository = userRepositoryOf(),
    unsentEventStorage: UnsentEventStorage = InMemoryUnsentEventStorage(),
    libraryConfiguration: LibraryConfiguration = libraryConfigurationOf(),
): PlaybackEventHandlerInteractor =
    PlaybackEventHandlerInteractor(
        userStorage = userStorage,
        userRepository = userRepository,
        unsentEventStorage = unsentEventStorage,
        libraryConfiguration = libraryConfiguration,
    )

internal class InMemoryUnsentEventStorage : UnsentEventStorage {
    private val advertisementEvents = mutableListOf<AdvertisementEvent>()
    override suspend fun getAllAdvertisementEvents(): List<AdvertisementEvent> = advertisementEvents
    private val playerEvents = mutableListOf<PlayerEvent>()
    override suspend fun getAllPlayerEvents(): List<PlayerEvent> = playerEvents
    override suspend fun add(advertisementEvent: AdvertisementEvent) { advertisementEvents.add(advertisementEvent) }
    override suspend fun add(playerEvent: PlayerEvent) { playerEvents.add(playerEvent) }
    override suspend fun remove(advertisementEvent: AdvertisementEvent) { advertisementEvents.remove(advertisementEvent) }
    override suspend fun remove(playerEvent: PlayerEvent) { playerEvents.remove(playerEvent) }
}
