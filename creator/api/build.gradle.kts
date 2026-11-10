plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.creator.grammar)
    implementation(projects.creator.semantic)
    implementation(projects.creator.parser.ll1)


    api(projects.api)
    api(projects.creator.backend.api)
    api(projects.creator.frontend.api)
    api(projects.creator.parser.api)
    api(projects.creator.logger.api)


    implementation(libs.kotlin.stdlib)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}