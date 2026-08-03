plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    api(project(":compiler:api"))
    api(project(":compiler:ast"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}