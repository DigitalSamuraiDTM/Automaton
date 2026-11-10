plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
