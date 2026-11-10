plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(projects.creator.parser.api)
    implementation(projects.creator.grammar)
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