plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.3.10"
}
group = "com.digitalsamurai.automaton"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
    }
}
kotlin {
    jvmToolchain(21)
}