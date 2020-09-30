object Constants {
    const val projectName = "AudioburstMobileLibrary"
    const val projectVersion = "0.0.1"

    object Library {
        const val packageName = "com.audioburst"
        const val version = projectVersion
    }

    object Cocoapods {
        const val summary = projectName
        const val homepage = "Link to GitHub"
    }

    object Android {
        const val compileSdkVersion = 29
        const val minSdkVersion = 21
        const val targetSdkVersion = 29
        const val versionName = projectVersion
        const val versionCode = 1
    }
}

object Dependencies {
    const val kotlinVersion = "1.4.10"

    object Plugins {
        const val multiplatform = "multiplatform"
        const val cocoapods = "native.cocoapods"
        const val androidLibrary = "com.android.library"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val mavenPublish = "maven-publish"
    }

    object Coroutines {
        private const val version = "1.3.9"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Serialization {
        private const val version = "1.0.0-RC2"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-serialization-core:$version"
    }

    object Ktor {
        private const val version = "1.4.1"
        const val core = "io.ktor:ktor-client-core:$version"
        const val json = "io.ktor:ktor-client-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"

        const val androidMain = "io.ktor:ktor-client-android:$version"
        const val iOSMain = "io.ktor:ktor-client-ios:$version"
        const val jsMain = "io.ktor:ktor-client-js:$version"
    }

    object SqlDelight {
        private const val version = "1.4.2"
        const val runtime = "com.squareup.sqldelight:runtime:$version"
        const val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:$version"

        const val androidMain = "com.squareup.sqldelight:android-driver:$version"
        const val iOSMain = "com.squareup.sqldelight:native-driver:$version"
    }

    object Common {

        object Test {
            const val testCommon = "test-common"
            const val testAnnotationsCommon = "test-annotations-common"
        }
    }
}