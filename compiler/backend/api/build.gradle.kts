plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    api(project(":compiler:semantic"))
    implementation(kotlin("stdlib-jdk8"))
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}