import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Dependencies.Plugins.androidLibrary)
    kotlin(Dependencies.Plugins.multiplatform) version Dependencies.kotlinVersion
    kotlin(Dependencies.Plugins.serialization) version Dependencies.kotlinVersion
    id(Dependencies.Plugins.swiftPackage) version Dependencies.Plugins.swiftPackageVersion
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
    android {
        publishLibraryVariants("release", "debug")
    }
    ios {
        binaries {
            framework {
                baseName = Constants.projectName
            }
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

                implementation(Dependencies.Stately.concurrency)
                implementation(Dependencies.Stately.isoCollections)
                implementation(Dependencies.Stately.isolate)
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
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

// PUBLISHING ANDROID

val sources by tasks.registering(Jar::class) {
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
            artifact(sources.get())

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

// PUBLISHING iOS

multiplatformSwiftPackage {
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("12") }
    }
    distributionMode {
        local()
    }
    buildConfiguration { release() }
    outputDirectory(File(projectDir.path))
}
