plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(projects.api)
    api(projects.compiler.frontend.api)
    implementation(projects.compiler.grammar)
    implementation(libs.kotlin.stdlib)
    implementation(libs.json)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}