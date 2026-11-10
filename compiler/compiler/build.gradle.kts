plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.compiler.grammar)
    implementation(projects.compiler.semantic)
    implementation(projects.compiler.parser.ll1)


    api(projects.api)
    api(projects.compiler.backend.api)
    api(projects.compiler.frontend.api)
    api(projects.compiler.parser.api)
    api(projects.compiler.logger.api)


    implementation(libs.kotlin.stdlib)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}