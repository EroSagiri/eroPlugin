import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose")
}

group = "me.sagiri"
version = "1.0"

repositories {
    mavenCentral()
}

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

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

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