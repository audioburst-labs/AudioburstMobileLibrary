plugins {
    kotlin(Dependencies.Plugins.multiplatform) version Dependencies.kotlinVersion
    kotlin(Dependencies.Plugins.cocoapods) version Dependencies.kotlinVersion
    kotlin(Dependencies.Plugins.serialization) version Dependencies.kotlinVersion
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
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    android {
        publishLibraryVariants("release", "debug")
    }
    val isBuildForPhysicalDevice = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (isBuildForPhysicalDevice) {
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

                implementation(Dependencies.Settings.commonMain)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Test.Common.testCommon)
                implementation(Dependencies.Test.Common.testAnnotationsCommon)
                implementation(Dependencies.Ktor.test)
                implementation(Dependencies.Settings.commonTest)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.jvmMain)
                implementation(Dependencies.SqlDelight.jvmMain)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependencies.Test.Jvm.junit)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.androidMain)
                implementation(Dependencies.SqlDelight.androidMain)
                implementation(Dependencies.Android.Startup.runtime)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.Test.Jvm.junit)
            }
        }
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
        val browserTest by getting {
            dependencies {
                implementation(Dependencies.Test.Js.js)
            }
        }
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
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
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xinline-classes"
}