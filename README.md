# Audioburst Mobile Library

## Introduction
AudioburstMobileLibrary is a multi platform library that allows convenient access to the Audioburst’s Content APIs. Playlists can be accessed, selected by users and additional information can be requested about the playlist. 

## Features
AudioburstMobileLibrary offers a simple API that lets you:
- Get all available playlists
- Get information about specific playlists, including lists of `Burst` 
- Handle sending required events to the API without any effort.

## Prerequisites

### Audioburst API key
The library requires an application key that can be obtained via [Audioburst Publishers](https://publishers.audioburst.com/).

## Get Started - Android

This guide is a quick walkthrough to add AudioburstMobileLibrary to an Android app. We recommend Android Studio as the development environment for building an app with the AudioburstMobileLibrary.

## Add AudioburstMobileLibrary to your app

### Step 1. Add AudioburstMobileLibrary dependency
[ ![Download Android](https://api.bintray.com/packages/audioburst/maven/mobile-library/images/download.svg) ](https://bintray.com/audioburst/maven/mobile-library/_latestVersion)

Add AudioburstMobileLibrary Android SDK to your project. To do this, add the following dependency in your app level `build.gradle` file:
```gradle
implementation 'com.audioburst:mobile-library:{latest-version}'
```

The library is built in Kotlin language and is using `Coroutines`, so to be able to support it you need to add the following configurations to your `android` script in the app level `build.config` file:

```gradle
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

In the event you are getting a "Duplicate class" on Kotlin Coroutines dependencies, you need to exclude those from the AudioburstPlayer library in the following way:
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

All the functions below are `suspending`, which means that you need to call them using a `CoroutineScope`. 
Additionally, they return the `Result` object which can be either `Data` or `Error`.
The library offers a few handy extension functions that make it easier to work with this type. Check the [Result](https://github.com/audioburst-labs/AudioburstMobileLibrary/blob/master/src/commonMain/kotlin/com/audioburst/library/models/Result.kt) class to learn more about it.  

## Get all available playlists
```kotlin
audioburstLibrary.getPlaylists()
    .onData { playlists ->
        // Display available playlists
    }
    .onError { error -> 
        // Handle error
    }
```

## Get playlist information
```kotlin
audioburstLibrary.getPlaylist(playlistItem)
    .onData { playlist ->
        // Build your playback queue by using list of Bursts
    }
    .onError { error -> 
        // Handle error
    }
```

## Get advertisement url
You can also play advertisements before playlist items (bursts.)
To do so, check if a specific `Burst` has an ad by calling `Burst.isAdAvailable`. 
If an ad is available then before playing the `Burst` content call `getAdUrl` which allows a Burst to be played with an ad. 
```kotlin
audioburstLibrary.getAdUrl(burst)
    .onData { adUrl ->
        // Play ad
    }
    .onError { error -> 
        // Handle error
    }
```

### Step 4. Inform library about current playback state
Audioburst is obligated to provide content owners comprehensive information about content playback, therefore all play events need to be reported. This library implements that functionality, and the only event required is to inform when playback starts and stops, and return the current playback state every time the library requests that information. 

If `MediaBrowserServiceCompat` is being used, we strongly recommend implementing the following functionalities there.  

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
This interface can be called by the library at any time, so please try to always return the current playback state, even when nothing is currently playing.
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

## Get Started - iOS

This guide is a quick walkthrough to add AudioburstMobileLibrary to an iOS app. We recommend XCode as the development environment for building an app with the AudioburstMobileLibrary.

### Requirements

- iOS 12.0+
- Xcode 11

## Add AudioburstMobileLibrary to your app

### Step 1. Add AudioburstMobileLibrary dependency
You can use [CocoaPods](http://cocoapods.org/) to install [AudioburstPlayer](https://cocoapods.org/pods/AudioburstMobileLibrary) by adding it to your `Podfile`:

```ruby
platform :ios, '12.0'
use_frameworks!

target 'MyApp' do
    pod 'AudioburstMobileLibrary', '~> 0.0.7'
end
```

### Step 2. Initialize `AudioburstLibrary` object
```swift
let audioburstLibrary = AudioburstLibrary(applicationKey: "YOUR_API_KEY_HERE")
```
You can use this class by instantiating its instance every time you need this or as a singleton.

### Step 3. Request Audioburst content

All the functions below lets you pass a closure that contains a requested data or error information.   

## Get all available playlists
```swift
audioburstLibrary.getPlaylists { playlists in
    // Display available playlists
} onError: { errorType in
    // Handle error
}
```

## Get playlist information
```swift
audioburstLibrary.getPlaylist(playlistInfo: playlistInfo) { playlist in
    // Build your playback queue by using list of Bursts
} onError: { errorType in
    // Handle error
}
```

## Get advertisement url
You can also play advertisements before playlist items (bursts.)
To do so, check if a specific `Burst` has an ad by calling `Burst.isAdAvailable`. 
If an ad is available then before playing the `Burst` content call `getAdUrl` which allows a Burst to be played with an ad. 
```swift
library.getAdUrl(burst: burst) { adUrl in
    // Play ad
} onError: { errorType in
    // Handle error
}
```

### Step 4. Inform library about current playback state
Audioburst is obligated to provide content owners comprehensive information about content playback, therefore all play events need to be reported. This library implements that functionality, and the only event required is to inform when playback starts and stops, and return the current playback state every time the library requests that information. 

## Playback start/stop
```swift
audioburstLibrary.start()
audioburstLibrary.stop()
```

If you are using iOS's `AVPlayer` then it can be easily done in a following way:
```swift
private var statusObservation: NSKeyValueObservation?

let playerItem = AVPlayerItem(url: url)
statusObservation = player?.observe(\.timeControlStatus, options: [.new, .old], changeHandler: { [weak self]
    (playerItem, change) in
    switch (playerItem.timeControlStatus) {
    case .playing:
        self?.audioburstLibrary.start()
    default:
        self?.audioburstLibrary.stop()
    }
})
player = AVPlayer(playerItem: playerItem)
```

## Returning current playback state
This interface can be called by the library at any time, so please try to always return the current playback state, even when nothing is currently playing.
```swift
audioburstLibrary.setPlaybackStateListener(playbackStateListener: self)

extension PlayerInteractor: PlaybackStateListener {
    func getPlaybackState() -> PlaybackState? {
        return PlaybackState(url: currentUrl, positionMillis: Int64(contentPositionMilis))
    }
}
```

## Full example using `AVPlayer`
We recommend initializing and keeping `PlayerInteractor` in `AppDelegate`. This way you are sure you can return a current playback state every time library ask for it. 
```swift
class PlayerInteractor {

    var player: AVPlayer?
    let audioburstLibrary = AudioburstLibrary(applicationKey: "123456")

    private var statusObservation: NSKeyValueObservation?

    init() {
        audioburstLibrary.setPlaybackStateListener(playbackStateListener: self)
        //get url and load item to play (...)
        let playerItem = AVPlayerItem(url: url)
        statusObservation = player?.observe(\.timeControlStatus, options: [.new, .old], changeHandler: { [weak self]
            (playerItem, change) in

            switch (playerItem.timeControlStatus) {
            case .playing:
                self?.audioburstLibrary.start()
            default:
                self?.audioburstLibrary.stop()
            }
        })

        player = AVPlayer(playerItem: playerItem)
    }
    
    deinit {
        audioburstLibrary.stop()
        audioburstLibrary.removePlaybackStateListener(playbackStateListener: self)
    }
}

extension PlayerInteractor: PlaybackStateListener {
    func getPlaybackState() -> PlaybackState? {
        guard let asset = (player?.currentItem?.asset) as? AVURLAsset, let player = self.player else {
            return nil
        }
        let url = asset.url.absoluteString
        let contentPositionMilis = (player.currentTime().seconds)/1000
        return PlaybackState(url: url, positionMillis: Int64(contentPositionMilis))
    }
}
```

## Privacy Policy
[Privacy Policy](https://audioburst.com/privacy)

## Terms of Service
[Terms of Service](https://audioburst.com/audioburst-publisher-terms)