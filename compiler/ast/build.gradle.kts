plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    api(project(":compiler:api"))
    api(project(":compiler:frontend:api"))
    implementation(project(":compiler:grammar"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.json:json:20260814")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}