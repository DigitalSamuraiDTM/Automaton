plugins {
    kotlin("jvm") version "2.2.20"
}


dependencies {
    api(project(":compiler:parser:api"))
    api(project(":compiler:grammar"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}