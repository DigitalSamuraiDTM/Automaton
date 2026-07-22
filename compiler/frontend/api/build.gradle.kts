plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":compiler:api"))
    api(project(":compiler:grammar"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

}

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}