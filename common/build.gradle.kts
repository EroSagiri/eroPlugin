plugins {
    kotlin("multiplatform") version "1.6.10"
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.6.10"
}

group = "me.sagiri"
version = "1.0.0"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.ui)
                api(compose.foundation)

                val decomposeVersion = "0.6.0"
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetpack:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-android:$decomposeVersion")

                val multiplatformSettingsVersion = "0.8.1"
                implementation("com.russhwolf:multiplatform-settings:${multiplatformSettingsVersion}")
                implementation("com.russhwolf:multiplatform-settings-serialization:${multiplatformSettingsVersion}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.4.1")
                api("androidx.core:core-ktx:1.7.0")
                api(compose.animation)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}

android {
    setCompileSdkVersion(32)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 32
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}