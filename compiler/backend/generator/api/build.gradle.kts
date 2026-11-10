plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(project(":compiler:semantic"))
    implementation(libs.kotlin.stdlib)
    api(project(":compiler:utils"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}