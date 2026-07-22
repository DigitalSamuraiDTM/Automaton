plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation(project(":compiler:frontend:api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}