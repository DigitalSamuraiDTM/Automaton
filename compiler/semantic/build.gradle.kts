plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    api(project(":compiler:api"))
    implementation(project(":compiler:ast"))
    api(project(":compiler:frontend:api"))
    api(project(":compiler:grammar"))
    implementation(kotlin("stdlib-jdk8"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}