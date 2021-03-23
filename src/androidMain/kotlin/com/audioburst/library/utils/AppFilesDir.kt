package com.audioburst.library.utils

import com.audioburst.library.applicationContext

internal actual fun getApplicationFilesDirectoryPath(): String = applicationContext.filesDir.absolutePath