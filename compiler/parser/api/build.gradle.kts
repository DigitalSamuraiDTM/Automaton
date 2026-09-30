plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    api(project(":compiler:api"))
    api(project(":compiler:ast"))
    api(project(":compiler:logger:api"))
    implementation(project(":compiler:grammar"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}