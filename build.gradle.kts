import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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
            }
        }
        val iosTest by getting
        val browserMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.jsMain)
                implementation(npm("uuid", "8.3.1"))
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
        consumerProguardFile("proguard-rules.pro")
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xinline-classes"
    kotlinOptions.freeCompilerArgs += "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
}

// PUBLISHING ANDROID

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

publishing {
    publications {
        create<MavenPublication>("aar") {
            groupId = Constants.Library.packageName
            artifactId = Constants.Library.archiveName
            version = Constants.Library.version

            artifact("$buildDir/outputs/aar/AudioburstMobileLibrary-release.aar")
            artifact(sourcesJar.get())

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.releaseImplementation.get().allDependencies.forEach {
                    dependenciesNode.appendNode("dependency").apply {
                        appendNode("groupId", it.group)
                        appendNode("artifactId", it.name)
                        appendNode("version", it.version)
                    }
                }
            }
            repositories {
                maven {
                    val user = gradleLocalProperties(rootDir).getProperty("bintray.user")
                    val apiKey = gradleLocalProperties(rootDir).getProperty("bintray.apikey")

                    url = uri("https://api.bintray.com/maven/$user/maven/${Constants.Library.archiveName}/;publish=0;override=1")
                    credentials {
                        username = user
                        password = apiKey
                    }
                }
            }
        }
    }
}

val assembleReleaseAndPublishToMavenRepository by tasks.registering {
    dependsOn("assembleRelease")
    dependsOn("generatePomFileForAarPublication")
    dependsOn("publishAarPublicationToMavenRepository")
    tasks.findByName("generatePomFileForAarPublication")?.mustRunAfter("assembleRelease")
    tasks.findByName("publishAarPublicationToMavenRepository")?.mustRunAfter("generatePomFileForAarPublication")
}

val assembleReleaseAndPublishToMavenLocal by tasks.registering {
    dependsOn("assembleRelease")
    dependsOn("generatePomFileForAarPublication")
    dependsOn("publishAarPublicationToMavenLocal")
    tasks.findByName("generatePomFileForAarPublication")?.mustRunAfter("assembleRelease")
    tasks.findByName("publishAarPublicationToMavenLocal")?.mustRunAfter("generatePomFileForAarPublication")
}
