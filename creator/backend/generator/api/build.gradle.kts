plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(projects.creator.semantic)
    implementation(libs.kotlin.stdlib)
    api(projects.creator.utils)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}