plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    api(project(":compiler:parser:api"))
    implementation(project(":compiler:grammar"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.json:json:20260814")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}