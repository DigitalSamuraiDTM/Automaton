plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(projects.compiler.parser.api)
    implementation(projects.compiler.grammar)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.json)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}