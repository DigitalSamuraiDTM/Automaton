plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    implementation(project(":compiler:semantic"))
    implementation(project(":compiler:backend:api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}