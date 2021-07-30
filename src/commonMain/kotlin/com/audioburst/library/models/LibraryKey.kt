package com.audioburst.library.models

import kotlin.jvm.JvmInline

@JvmInline
internal value class LibraryKey(val value: String)

internal expect val platformLibraryKey: LibraryKey