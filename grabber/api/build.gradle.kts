plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(projects.api)
    api(projects.grabber.representation)
    api(projects.grabber.storage.api)
    implementation(libs.kotlin.stdlib)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
