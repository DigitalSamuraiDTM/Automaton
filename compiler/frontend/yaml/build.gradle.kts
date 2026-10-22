plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation(project(":compiler:frontend:api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation(project(":compiler:grammar"))

    // Source: https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation("org.yaml:snakeyaml:2.6")
    implementation("org.json:json:20260814")

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}