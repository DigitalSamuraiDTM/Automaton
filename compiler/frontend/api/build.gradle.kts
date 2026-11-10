plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(project(":api"))
    implementation(project(":compiler:grammar"))
    implementation(libs.kotlinx.coroutines.core)

}

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}