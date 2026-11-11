plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    implementation(projects.grabber.frontend.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.swagger.parser)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
