pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("android").version(extra["kotlin.version"] as String)
        id("com.android.application").version(extra["agp.version"] as String)
        id("com.android.library").version(extra["agp.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.jetbrains.kotlin.plugin.parcelize").version(extra["kotlin.version"] as String)
        kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
        id("com.github.johnrengelman.shadow").version(extra["shadow.version"] as String)
    }

}
rootProject.name = "ero"
include("loliapi")
include("plugin")
include(":android")
include("common")
include("desktop")
