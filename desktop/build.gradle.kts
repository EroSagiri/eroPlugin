import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "1.6.10"
    id("org.jetbrains.compose")
}

group = "com.rocsss.avp"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            dependencies {
                implementation(project(":common"))
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.animation)
                implementation(compose.foundation)
                implementation(compose.preview)
                implementation(compose.uiTooling)
                implementation(compose.uiTestJUnit4)
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.common)

                val decomposeVersion = "0.6.0"
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetpack:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-android:$decomposeVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        jvmArgs += "-Djava.net.preferIPv4Stack=true"
        mainClass = "me.sagiri.ero.desktop.Application"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb, TargetFormat.AppImage)
            packageName = "AVSManage"
            packageVersion = "1.0.0"

            windows {
                shortcut = true
                menuGroup = "rocsss"
                menu = true
            }

            linux {
                shortcut = true
                menuGroup = "rocsss"
            }
        }
    }
}
/*
@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
dependencies {
    implementation(project(":common"))

    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.animation)
    implementation(compose.foundation)
    implementation(compose.preview)
    implementation(compose.uiTooling)
    implementation(compose.uiTestJUnit4)
    implementation(compose.desktop.currentOs)
    implementation(compose.desktop.common)

    val decomposeVersion = "0.6.0"
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetpack:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-android:$decomposeVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}
 */

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "me.sagiri.ero.desktop.Application"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "ero"
            packageVersion = "1.0.0"

            windows {
                shortcut = true
                menuGroup = "sagiri"
                menu = true
            }

            linux {
                shortcut = true
                menuGroup = "sagiri"
            }
        }
    }
}