object Constants {
    const val projectName = "AudioburstMobileLibrary"
    const val projectVersion = "0.0.10"

    object Library {
        const val packageName = "com.audioburst"
        const val version = projectVersion
        const val archiveName = "mobile-library"
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
    const val kotlinVersion = "1.4.20"

    private fun kotlin(dependency: String): String = "org.jetbrains.kotlin:$dependency:$kotlinVersion"

    object Plugins {
        const val multiplatform = "multiplatform"
        const val cocoapods = "native.cocoapods"
        const val androidLibrary = "com.android.library"
        const val mavenPublish = "maven-publish"
        const val serialization = "plugin.serialization"
        const val swiftPackageVersion = "1.0.2"
        const val swiftPackage = "com.chromaticnoise.multiplatform-swiftpackage"
    }

    object Coroutines {
        private const val version = "1.4.1-native-mt"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val androidMain = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Serialization {
        private const val version = "1.0.0"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
    }

    object Ktor {
        private const val version = "1.4.2"
        const val core = "io.ktor:ktor-client-core:$version"
        const val json = "io.ktor:ktor-client-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"
        const val test = "io.ktor:ktor-client-tests:$version"

        const val androidMain = "io.ktor:ktor-client-android:$version"
        const val iOSMain = "io.ktor:ktor-client-ios:$version"
    }

    object SqlDelight {
        private const val version = "1.4.2"
        const val runtime = "com.squareup.sqldelight:runtime:$version"
        const val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:$version"

        const val jvmMain = "com.squareup.sqldelight:sqlite-driver:$version"
        const val androidMain = "com.squareup.sqldelight:android-driver:$version"
        const val iOSMain = "com.squareup.sqldelight:native-driver:$version"
    }

    object Settings {
        private const val version = "0.6.2"
        const val commonMain = "com.russhwolf:multiplatform-settings:$version"
        const val commonTest = "com.russhwolf:multiplatform-settings-test:$version"
    }

    object Stately {
        const val concurrency =  "co.touchlab:stately-concurrency:1.1.1"
        const val isoCollections =  "co.touchlab:stately-iso-collections:1.1.1-a1"
        const val isolate =  "co.touchlab:stately-isolate:1.1.1-a1"
    }

    object Android {
        object Startup {
            private const val version = "1.0.0"
            const val runtime = "androidx.startup:startup-runtime:$version"
        }
    }

    object Test {

        object Common {
            val testCommon = kotlin("kotlin-test-common")
            val testAnnotationsCommon = kotlin("kotlin-test-annotations-common")
        }

        object Jvm {
            val junit = kotlin("kotlin-test-junit")
        }
    }
}