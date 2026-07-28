plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    api(project(":compiler:api"))
    api(project(":compiler:backend:api"))
    api(project(":compiler:frontend:api"))
    implementation(kotlin("stdlib-jdk8"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}