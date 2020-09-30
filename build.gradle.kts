plugins {
    kotlin(Dependencies.Plugins.multiplatform) version Dependencies.kotlinVersion
    kotlin(Dependencies.Plugins.cocoapods) version Dependencies.kotlinVersion
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroidExtensions)
    id(Dependencies.Plugins.mavenPublish)
}
group = Constants.Library.packageName
version = Constants.Library.version

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
kotlin {
    cocoapods {
        summary = Constants.Cocoapods.summary
        homepage = Constants.Cocoapods.homepage
    }
    android {
        publishLibraryVariants("release", "debug")
    }
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }
    js("browser") {
        browser {
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Coroutines.commonMain)

                implementation(Dependencies.Serialization.commonMain)

                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.json)
                implementation(Dependencies.Ktor.logging)
                implementation(Dependencies.Ktor.serialization)

                implementation(Dependencies.SqlDelight.runtime)
                implementation(Dependencies.SqlDelight.coroutinesExtensions)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin(Dependencies.Common.Test.testCommon))
                implementation(kotlin(Dependencies.Common.Test.testAnnotationsCommon))

                implementation(Dependencies.Coroutines.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.androidMain)
                implementation(Dependencies.SqlDelight.androidMain)
            }
        }
        val androidTest by getting
        val iosMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.iOSMain)
                implementation(Dependencies.SqlDelight.iOSMain)
            }
        }
        val iosTest by getting
        val browserMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.jsMain)
            }
        }
        val browserTest by getting
    }
}
android {
    compileSdkVersion(Constants.Android.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Constants.Android.minSdkVersion)
        targetSdkVersion(Constants.Android.targetSdkVersion)
        versionCode = Constants.Android.versionCode
        versionName = Constants.Android.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}