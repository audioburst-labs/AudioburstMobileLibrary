import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Dependencies.Plugins.androidLibrary)
    kotlin(Dependencies.Plugins.multiplatform) version Dependencies.kotlinVersion
    kotlin(Dependencies.Plugins.serialization) version Dependencies.kotlinVersion
    id(Dependencies.Plugins.swiftPackage) version Dependencies.Plugins.swiftPackageVersion
    id(Dependencies.Plugins.mavenPublish)
    id(Dependencies.Plugins.signing)
    id(Dependencies.Plugins.sqlDelight) version Dependencies.sqlDelightVersion
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

                implementation(Dependencies.Stately.concurrency)
                implementation(Dependencies.Stately.isoCollections)
                implementation(Dependencies.Stately.isolate)

                implementation(Dependencies.SqlDelight.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Test.Common.testCommon)
                implementation(Dependencies.Test.Common.testAnnotationsCommon)
                implementation(Dependencies.Ktor.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.androidMain)
                implementation(Dependencies.Coroutines.androidMain)
                implementation(Dependencies.Android.Startup.runtime)
                implementation(Dependencies.SqlDelight.androidMain)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.Test.Jvm.junit)
                implementation(Dependencies.SqlDelight.jvm)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.iOSMain)
                implementation(Dependencies.SqlDelight.iOSMain)
            }
        }
        val iosTest by getting {
            dependencies {
                implementation(Dependencies.SqlDelight.iOSMain)
            }
        }
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
        }
        dependencies {
            coreLibraryDesugaring(Dependencies.Android.JdkDesugar.desugar)
        }
    }
}
sqldelight {
    database(name = "Database") {
        packageName = Constants.Library.packageName
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
    compileOptions {
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += "-Xinline-classes"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
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

            pom {
                name.set(artifactId)
                description.set("AudioburstMobileLibrary is a multi platform library that allows convenient access to the Audioburst’s Content APIs.")
                url.set("https://github.com/audioburst-labs/AudioburstMobileLibrary")
                licenses {
                    license {
                        name.set("Terms of Service")
                        url.set("https://audioburst.com/audioburst-publisher-terms")
                    }
                }
                developers {
                    developer {
                        id.set("Kamil-H")
                        name.set("Kamil Halko")
                        email.set("kamil@audioburst.com")
                    }
                }
                scm {
                    url.set("https://github.com/audioburst-labs/AudioburstMobileLibrary/tree/master")
                }
                withXml {
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
                        url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                        credentials {
                            username = gradleLocalProperties(rootDir).getProperty("ossrhUsername")
                            password = gradleLocalProperties(rootDir).getProperty("ossrhPassword")
                        }
                    }
                }
            }
        }
    }
}

ext["signing.keyId"] = gradleLocalProperties(rootDir).getProperty("signing.keyId")
ext["signing.secretKeyRingFile"] = gradleLocalProperties(rootDir).getProperty("signing.secretKeyRingFile")
ext["signing.password"] = gradleLocalProperties(rootDir).getProperty("signing.password")

signing {
    sign(publishing.publications)
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
