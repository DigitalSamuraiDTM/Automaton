plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation(project(":compiler:frontend:api"))
    implementation(kotlin("stdlib-jdk8"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}