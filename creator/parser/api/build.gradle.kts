plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(projects.api)
    api(projects.creator.ast)
    api(projects.creator.logger.api)
    implementation(projects.creator.grammar)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}