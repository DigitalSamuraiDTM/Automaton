plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.grabber.frontend.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.snakeyaml)
    implementation(libs.json)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
