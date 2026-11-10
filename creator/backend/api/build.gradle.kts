plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(projects.api)
    api(projects.creator.backend.generator.api)
    api(projects.creator.backend.integrator.api)
    api(projects.creator.semantic)
    api(projects.creator.logger.api)
    implementation(projects.creator.utils)

    implementation(libs.kotlin.stdlib)
    implementation(libs.json)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}