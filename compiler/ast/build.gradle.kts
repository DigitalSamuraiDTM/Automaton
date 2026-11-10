plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(project(":api"))
    api(project(":compiler:frontend:api"))
    implementation(project(":compiler:grammar"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.json)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}