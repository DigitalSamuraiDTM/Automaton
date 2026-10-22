plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    implementation(project(":compiler:api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Source: https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20260814")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}