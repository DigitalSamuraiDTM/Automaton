plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.compiler.semantic)
    implementation(projects.compiler.backend.generator.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}