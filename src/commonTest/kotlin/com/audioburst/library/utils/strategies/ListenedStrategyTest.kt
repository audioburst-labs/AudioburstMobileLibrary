package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.downloadedAdvertisementOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.*
import kotlin.test.Test
import kotlin.test.assertTrue

class ListenedStrategyTest {

    @Test
    fun testIfTwoSecPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 3.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    )
                )
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
    }

    @Test
    fun testIfPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 10.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    )
                )
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().isNotEmpty())
    }

    @Test
    fun testIfTwoSecADPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 3.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        adUrl = url,
                    )
                )
            ),
            advertisements = listOf(
                downloadedAdvertisementOf(
                    advertisement = advertisementOf(
                        burstUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    ),
                    downloadUrl = Url(url)
                ),
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecADPlaying>().isNotEmpty())
    }

    @Test
    fun testIfMultipleEventsAreGettingDetected() {
        // GIVEN
        val url = "https://storageaudiobursts.azureedge.net/audio/J8OoDzXD9Pwn.mp3"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = url,
                    duration = 48.0.toDuration(DurationUnit.Seconds)
                )
            )
        )
        val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(10)
        val listenedStrategy = listenedStrategyOf(
            creator = InputBasedPlaybackPeriodsCreator(),
            factory = listenedMediaStrategyFactoryOf(
                2.0.toDuration(DurationUnit.Seconds)
            ),
        )
        val now = PlatformTimestampProvider.timeSince1970()

        // WHEN
        val events = listOf(
            1.240, 3.243, 5.245, 7.247, 9.249, 11.252, 13.254, 15.256, 17.258, 19.260, 21.262, 23.264, 25.266, 27.268,
            29.270, 31.272, 33.274, 35.276, 37.278, 39.280, 41.282, 43.285, 45.287,
        ).flatMap { position ->
            val input = inputOf(
                currentState = playbackStateOf(
                    url = url,
                    position = position,
                    occurrenceTime = (now + position.toDuration(DurationUnit.Seconds)).milliseconds.toLong()
                ),
                playlist = playlist,
                previousStates = previousStates,
            )
            listenedStrategy.check(input).apply {
                previousStates.add(input.currentState)
            }
        }

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 4)
    }

    @Test
    fun testIfMultipleEventsAreGettingDetected1() {
        // GIVEN
        val url = "https://storageaudiobursts.azureedge.net/audio/J8OoDzXD9Pwn.mp3"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = url,
                    duration = 48.0.toDuration(DurationUnit.Seconds)
                )
            )
        )
        val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(10)
        val listenedStrategy = listenedStrategyOf(
            creator = InputBasedPlaybackPeriodsCreator(),
            factory = listenedMediaStrategyFactoryOf(
                2.0.toDuration(DurationUnit.Seconds)
            ),
        )

        // WHEN
        val times = listOf(
            1625473267705, 1625473269710, 1625473271714, 1625473273717, 1625473275722, 1625473277731, 1625473279736,
            1625473281741, 1625473283746, 1625473285750, 1625473287755, 1625473289760, 1625473291764, 1625473293769,
            1625473295774, 1625473297778, 1625473299783, 1625473301788, 1625473303793, 1625473305797, 1625473307804,
            1625473309807, 1625473311812, 1625473313816, 1625473315821, 1625473317826,
        )
        val events = listOf(
            2.005, 4.010, 6.016, 8.021, 10.026, 12.032, 14.037, 16.042, 18.048, 20.053, 22.058, 24.064, 26.069, 28.074,
            30.080, 32.085, 34.090, 36.096, 38.101, 40.106, 42.112, 44.117, 46.122, 48.128, 50.133, 52.138,
        ).flatMapIndexed { index, position ->
            val input = inputOf(
                currentState = playbackStateOf(
                    url = url,
                    position = position,
                    occurrenceTime = times[index]
                ),
                playlist = playlist,
                previousStates = previousStates,
            )
            listenedStrategy.check(input).apply {
                previousStates.add(input.currentState)
            }
        }

        // THEN
        println(events.joinToString { it.actionName })
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 4)
    }

    @Test
    fun testIfMultipleEventsAreGettingDetected2() {
        // GIVEN
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = "poWv9OJ8DgwG",
                    audioUrl = "https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3",
                    duration = 25.0.toDuration(DurationUnit.Seconds)
                ),
                burstOf(
                    id = "69ja939Pa726",
                    audioUrl = "https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3",
                    duration = 29.0.toDuration(DurationUnit.Seconds)
                ),
                burstOf(
                    id = "poWgqPY9YaXl",
                    audioUrl = "https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3",
                    duration = 73.0.toDuration(DurationUnit.Seconds)
                ),
            )
        )
        val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(10)
        val listenedStrategy = listenedStrategyOf(
            creator = InputBasedPlaybackPeriodsCreator(),
            factory = listenedMediaStrategyFactoryOf(
                2.0.toDuration(DurationUnit.Seconds)
            ),
        )

        // WHEN
        val events = """
            1625668794070,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,2.005
            1625668796051,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,4.031
            1625668798056,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,6.037
            1625668800058,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,8.021
            1625668802062,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,10.026
            1625668804065,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,12.031
            1625668806067,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,14.037
            1625668808070,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,16.042
            1625668810074,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,18.048
            1625668812078,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,20.053
            1625668814083,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,22.058
            1625668816085,https://storageaudiobursts.azureedge.net/audio/poWv9OJ8DgwG.mp3,24.063
            1625668821820,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,2.005
            1625668823820,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,4.01
            1625668825824,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,6.016
            1625668827828,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,8.021
            1625668829832,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,10.026
            1625668831836,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,12.031
            1625668833840,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,14.037
            1625668835848,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,16.042
            1625668837847,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,18.048
            1625668839858,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,20.053
            1625668841863,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,22.058
            1625668843867,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,24.063
            1625668845870,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,26.069
            1625668847873,https://storageaudiobursts.azureedge.net/audio/69ja939Pa726.mp3,28.074
            1625668853287,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,2.005
            1625668855288,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,4.01
            1625668857292,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,6.016
            1625668859299,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,8.021
            1625668861303,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,10.026
            1625668863309,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,12.032
            1625668865313,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,14.037
            1625668867318,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,16.042
            1625668869323,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,18.048
            1625668871329,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,20.053
            1625668873336,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,22.058
            1625668875341,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,24.064
            1625668877346,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,26.069
            1625668879350,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,28.074
            1625668881353,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,30.08
            1625668883357,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,32.085
            1625668885362,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,34.09
            1625668887367,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,36.096
            1625668889372,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,38.101
            1625668891377,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,40.106
            1625668893382,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,42.112
            1625668895398,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,44.117
            1625668897393,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,46.122
            1625668899397,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,48.128
            1625668901401,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,50.133
            1625668903406,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,52.138
            1625668905413,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,54.144
            1625668907417,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,56.149
            1625668909443,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,58.154
            1625668911643,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,60.16
            1625668913434,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,62.165
            1625668915439,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,64.17
            1625668917445,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,66.176
            1625668919451,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,68.181
            1625668921457,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,70.186
            1625668923462,https://storageaudiobursts.azureedge.net/audio/poWgqPY9YaXl.mp3,72.192
        """.trimIndent()
            .lines()
            .map { it.split(",") }
            .flatMap {
                val occurrenceTime = it[0].toLong()
                val url = it[1]
                val position = it[2].toDouble()
                val input = inputOf(
                    currentState = playbackStateOf(
                        url = url,
                        position = position,
                        occurrenceTime = occurrenceTime,
                    ),
                    playlist = playlist,
                    previousStates = previousStates,
                )
                listenedStrategy.check(input).apply {
                    previousStates.add(input.currentState)
                }
            }

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().size == 3)
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().size == 3)
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 11)
    }

    @Test
    fun testIfMultipleEventsAreGettingDetected3() {
        // GIVEN
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = "699v5DwkWP36",
                    audioUrl = "https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8",
                    duration = 25.0.toDuration(DurationUnit.Seconds)
                ),
            )
        )
        val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(10)
        val listenedStrategy = listenedStrategyOf(
            creator = InputBasedPlaybackPeriodsCreator(),
            factory = listenedMediaStrategyFactoryOf(
                2.0.toDuration(DurationUnit.Seconds)
            ),
        )

        // WHEN
        val events = """
            1625653233579,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,1.98
            1625653235582,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,3.983
            1625653237585,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,5.986
            1625653239587,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,7.988
            1625653241590,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,9.991
            1625653243592,https://storageaudiobursts.azureedge.net/stream/699v5DwkWP36/outputlist.m3u8,11.994
        """.trimIndent()
            .lines()
            .map { it.split(",") }
            .flatMap {
                val occurrenceTime = it[0].toLong()
                val url = it[1]
                val position = it[2].toDouble()
                val input = inputOf(
                    currentState = playbackStateOf(
                        url = url,
                        position = position,
                        occurrenceTime = occurrenceTime,
                    ),
                    playlist = playlist,
                    previousStates = previousStates,
                )
                listenedStrategy.check(input).apply {
                    previousStates.add(input.currentState)
                }
            }

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().size == 1)
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().size == 1)
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 1)
    }
}
