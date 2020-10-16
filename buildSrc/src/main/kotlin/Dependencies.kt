object Constants {
    const val projectName = "AudioburstMobileLibrary"
    const val projectVersion = "0.0.1"

    object Library {
        const val packageName = "com.audioburst"
        const val version = projectVersion
    }

    object Cocoapods {
        const val summary = projectName
        const val homepage = "Link to GitHub" // TODO: Provide url to GitHub when it's set up
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

    private fun kotlin(dependency: String): String = "org.jetbrains.kotlin:$dependency:$kotlinVersion"

    object Plugins {
        const val multiplatform = "multiplatform"
        const val cocoapods = "native.cocoapods"
        const val androidLibrary = "com.android.library"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val mavenPublish = "maven-publish"
        const val serialization = "plugin.serialization"
    }

    object Coroutines {
        private const val version = "1.3.9-native-mt!!"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Serialization {
        private const val version = "1.0.0"
        const val commonMain = "org.jetbrains.kotlinx:kotlinx-serialization-json:$version"
    }

    object Ktor {
        private const val version = "1.4.1"
        const val core = "io.ktor:ktor-client-core:$version"
        const val json = "io.ktor:ktor-client-json:$version"
        const val logging = "io.ktor:ktor-client-logging:$version"
        const val serialization = "io.ktor:ktor-client-serialization:$version"
        const val test = "io.ktor:ktor-client-tests:$version"

        const val jvmMain = "io.ktor:ktor-client-apache:$version"
        const val androidMain = "io.ktor:ktor-client-android:$version"
        const val iOSMain = "io.ktor:ktor-client-ios:$version"
        const val jsMain = "io.ktor:ktor-client-js:$version"
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

    object Android {
        object Startup {
            private const val version = "1.0.0-rc01"
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

        object Js {
            val js = kotlin("kotlin-test-js")
        }
    }
}