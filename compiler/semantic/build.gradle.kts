plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(project(":api"))
    api(project(":compiler:frontend:api"))
    api(project(":compiler:logger:api"))

    implementation(project(":compiler:ast"))
    implementation(project(":compiler:grammar"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.json)
    implementation(project(":compiler:utils"))

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}