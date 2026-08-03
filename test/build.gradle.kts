plugins {
    kotlin("jvm") version "2.2.20"
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(project(":compiler:compiler"))
    implementation(project(":compiler:frontend:yaml"))
    implementation(project(":compiler:parser:ll1"))

    implementation(kotlin("stdlib-jdk8"))

    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
}