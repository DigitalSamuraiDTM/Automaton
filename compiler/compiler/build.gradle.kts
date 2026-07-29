plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation(project(":compiler:grammar"))
    implementation(project(":compiler:parser:ll1"))


    api(project(":compiler:api"))
    api(project(":compiler:backend:api"))
    api(project(":compiler:frontend:api"))
    api(project(":compiler:parser:api"))


    implementation(kotlin("stdlib-jdk8"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}