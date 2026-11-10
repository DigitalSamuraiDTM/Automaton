plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
}
group = "com.digitalsamurai.automaton"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
    }
}
kotlin {
    jvmToolchain(21)
}