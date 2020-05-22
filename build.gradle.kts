import com.palantir.gradle.gitversion.VersionDetails

plugins {
    id("org.jetbrains.intellij") version "0.4.16"
    id("net.researchgate.release") version "2.8.1"
    id("com.palantir.git-version") version "0.12.3"
    java
    kotlin("jvm") version "1.3.61"
}

val version: String by project

group = "me.vnagy.intellijplugins"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.1"
    setPlugins("yaml")
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

fun getVersionDetails(): VersionDetails = (extra["versionDetails"] as groovy.lang.Closure<*>)() as VersionDetails

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    publishPlugin {
        token(System.getenv("INTELLIJ_PUBLISH_TOKEN"))
        channels(System.getenv("INTELLIJ_PLUGIN_CHANNEL"))
    }
    patchPluginXml {
        val details = getVersionDetails()
        val pluginVersion: String
        if (details.isCleanTag) {
            pluginVersion = version
        } else {
            pluginVersion = "$version-${details.gitHash}"
        }
        changeNotes(file("change-notes.html"))
        version(pluginVersion)
    }
}
