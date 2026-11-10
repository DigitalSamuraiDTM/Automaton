plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(project(":api"))
    api(project(":compiler:backend:generator:api"))
    api(project(":compiler:logger:api"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.json)

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}