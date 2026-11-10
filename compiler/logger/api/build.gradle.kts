plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(project(":api"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}