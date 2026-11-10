plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":compiler:grammar"))
    implementation(project(":compiler:semantic"))
    implementation(project(":compiler:parser:ll1"))


    api(project(":api"))
    api(project(":compiler:backend:api"))
    api(project(":compiler:frontend:api"))
    api(project(":compiler:parser:api"))
    api(project(":compiler:logger:api"))


    implementation(libs.kotlin.stdlib)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}