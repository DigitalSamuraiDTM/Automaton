plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(projects.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}