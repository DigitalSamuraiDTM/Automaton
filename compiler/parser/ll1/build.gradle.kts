plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(project(":compiler:parser:api"))
    implementation(project(":compiler:grammar"))
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