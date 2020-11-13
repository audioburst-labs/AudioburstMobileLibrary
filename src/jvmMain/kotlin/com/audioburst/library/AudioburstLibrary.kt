package com.audioburst.library

actual class AudioburstLibrary actual constructor(applicationKey: String) :
    CoroutineAudioburstLibrary by AudioburstLibraryDelegate(applicationKey)