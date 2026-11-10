plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(projects.api)
    api(projects.grabber.frontend.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
