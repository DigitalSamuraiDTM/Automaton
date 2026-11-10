plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(projects.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    // Source: https://mvnrepository.com/artifact/org.json/json
    implementation(libs.json)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}