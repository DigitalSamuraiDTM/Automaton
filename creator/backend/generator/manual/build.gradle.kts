plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.creator.semantic)
    implementation(projects.creator.backend.generator.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}