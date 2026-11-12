plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(projects.grabber.api)
    api(projects.grabber.storage.api)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.kotlin.stdlib)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
