package com.audioburst.library

actual class AudioburstLibrary actual constructor(applicationKey: String) :
    CallbackAudioburstLibrary by AudioburstLibraryDelegate(applicationKey)