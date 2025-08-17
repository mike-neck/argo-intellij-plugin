import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.intellij.platform") version "2.7.1"
//    id("net.researchgate.release") version "2.8.1"
//    id("com.palantir.git-version") version "0.12.3"
    id("java")
    kotlin("jvm") version "2.1.20"
}

group = "me.vnagy.intellijplugins"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    intellijPlatform {
        intellijIdeaUltimate("2025.2")
        bundledPlugin("com.intellij.kubernetes")
        bundledPlugin("org.jetbrains.plugins.yaml")
        bundledPlugin("com.intellij.modules.json")
        plugin("org.jetbrains.plugins.go-template:252.23892.201")

        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Argo Workflow Editor"
        version.set(provider { project.version.toString() })
        description = "A plugin which helps to edit <a href=\"https://github.com/argoproj/argo\">Argo</a> workflow yaml files."
        ideaVersion {
            sinceBuild = "252"
            untilBuild = "253.*"
        }
    }
}
