plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    api(projects.grabber.api)
    implementation(libs.kotlin.stdlib)
    api(libs.swagger.parser)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
}
