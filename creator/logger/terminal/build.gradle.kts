plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    api(projects.creator.logger.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}