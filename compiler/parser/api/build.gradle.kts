plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(projects.api)
    api(projects.compiler.ast)
    api(projects.compiler.logger.api)
    implementation(projects.compiler.grammar)
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