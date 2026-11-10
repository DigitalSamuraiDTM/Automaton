plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(projects.compiler.semantic)
    implementation(libs.kotlin.stdlib)
    api(projects.compiler.utils)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}