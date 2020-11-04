# Audioburst Mobile Library

## Introduction
AudioburstMobileLibrary is the multiplatform library that eases access to the Audioburst API. You can easily download available playlists, let your user choose one and request additional information about the playlist. 

## Features
AudioburstMobileLibrary offers simple API that will let you:
- get all available playlists,
- get information about specific playlist, inlcuding list of `Burst`,
- handle sending required events to the API without any effort.

## Prerequisites

### Audioburst API key
The library requires an application key, which can be obtained via [Audioburst Publishers](https://publishers.audioburst.com/).

## Get Started - Android

This guide is a quick walkthrough to add AudioburstMobileLibrary to an Android app. We recommend Android Studio as the development environment for building an app with the AudioburstMobileLibrary.

## Add AudioburstMobileLibrary to your app

### Step 1. Add AudioburstMobileLibrary dependency
[ ![Download Android](https://api.bintray.com/packages/audioburst/maven/mobile-library/images/download.svg) ](https://bintray.com/audioburst/maven/mobile-library/_latestVersion)

Add AudioburstMobileLibrary Android SDK to your project. To do this, add the following dependency in your app level `build.gradle` file:
```gradle
implementation 'com.audioburst:mobile-library:{latest-version}'
```

The library is built in Kotlin language and is using `Coroutines`, so to be able to support it you need to add following configurations to your `android` script in app level `build.config` file:
```gradle
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

In case your are getting a "Duplicate class" on Kotlin Coroutines dependencies, you need to exclude those from AudioburstPlayer library in a following way:
```gradle
implementation ("com.audioburst:mobile-library:{latest-version}") {
    exclude group: "org.jetbrains.kotlinx", module: "kotlinx-coroutines-core-jvm"
}
```

### Step 2. Initialize `AudioburstLibrary` object
```kotlin
val audioburstLibrary: AudioburstLibrary = AudioburstLibrary("YOUR_API_KEY_HERE")
```
You can use this class by instantiating its instance every time you need this or as a singleton.

### Step 3. Request Audioburst content

All the functions below are `suspending`, which means that you need to call them in some `CoroutineScope`. Additionally, they are returning the `Result` object which can be either `Data` or `Error`. Library offers few handy extension functions that will make it easy to work with this type. Check `Result` class to learn more about it.  

## Get all available playlists
```kotlin
audioburstLibrary.getPlaylists()
    .onData { playlists ->
        // Display available playlists
    }
    .onError { type -> 
        // Handle error
    }
```

## Get playlist information
```kotlin
audioburstLibrary.getPlaylist(playlistItem)
    .onData { playlist ->
        // Build your playback queue by using list of Bursts
    }
    .onError { type -> 
        // Handle error
    }
```

## Get advertisement url
You can also play advertisements. To do so, you need to check if specific `Burst` has ad by calling `Burst.isAdAvailable`. If it's available then just before playing this `Burst` content you need to get `adUrl` which let you play `Burst` with ad. 
```kotlin
audioburstLibrary.getAdUrl(burst)
    .onData { adUrl ->
        // Play ad
    }
    .onError { type -> 
        // Handle error
    }
```

### Step 4. Inform library about current playback state
To give you the best quality content, Audioburst needs information about user likes to listen and what no. To make it possible to detect such behaviours we need to receive some events that are generated by the user. Detecting so events is not so easy task, that is why this library implements this functionality. The only thing you need to do is to inform us when playback starts and stops and implement one interface and return a current playback state in it everytime library is asking for it.

If you are using `MediaBrowserServiceCompat`, then we strongly recommend implementing following functionalities there.  

## Playback start/stop
```kotlin
audioburstLibrary.start()
audioburstLibrary.stop()
```

If you are using Android's `ExoPlayer` then it can be easily done by implementing `EventListener.onIsPlayingChanged`:
```kotlin
override fun onIsPlayingChanged(isPlaying: Boolean) {
    super.onIsPlayingChanged(isPlaying)
    mediaSessionConnection.exoPlayerCallback?.onIsPlayingChanged(isPlaying)
    if (isPlaying) {
        audioburstLibrary.start()
    } else {
        audioburstLibrary.stop()
    }
}
```

## Returning current playback state
This interface can be called by the library at any time, so please try always return current playback state, even if it is not playing anything currently.
```kotlin
audioburstLibrary.setPlaybackStateListener(this)

override fun getPlaybackState(): PlaybackState? {
    return PlaybackState(
        positionMillis = contentPosition,
        url = currentUrl
    )
}
```

## Full example using `MediaBrowserServiceCompat` and `ExoPlayer`
```kotlin
class MediaService : MediaBrowserServiceCompat(), Player.EventListener, PlaybackStateListener {

    private val exoPlayer: ExoPlayer = ...
    private val audioburstLibrary: AudioburstLibrary = ...
    private var currentPlaylist: List<String> = ...

    override fun onCreate() {
        super.onCreate()
        (...)
        exoPlayer.addListener(this)
        audioburstLibrary.setPlaybackStateListener(this)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) {
            audioburstLibrary.start()
        } else {
            audioburstLibrary.stop()
        }
    }

    override fun onDestroy() {
        audioburstLibrary.stop()
        audioburstLibrary.removePlaybackStateListener(this)
    }

    override fun getPlaybackState(): PlaybackState? {
        val currentIndex = exoPlayer.currentTimeline.getWindow(exoPlayer.currentWindowIndex, Timeline.Window())
        return PlaybackState(
            positionMillis = exoPlayer.contentPosition,
            url = currentPlaylist[currentIndex]
        )
    }
    
    (...)
}
```

## Privacy Policy
[Privacy Policy](https://audioburst.com/privacy)

## Terms of Service
[Terms of Service](https://audioburst.com/audioburst-publisher-terms)