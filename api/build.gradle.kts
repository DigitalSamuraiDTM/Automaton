plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlin.stdlib)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}