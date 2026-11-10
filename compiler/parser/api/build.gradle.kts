plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(project(":api"))
    api(project(":compiler:ast"))
    api(project(":compiler:logger:api"))
    implementation(project(":compiler:grammar"))
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