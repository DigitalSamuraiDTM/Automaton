plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(projects.grabber.representation)
    implementation(libs.kotlin.stdlib)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
