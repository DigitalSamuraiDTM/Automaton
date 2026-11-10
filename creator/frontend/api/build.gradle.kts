plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(projects.api)
    implementation(projects.creator.grammar)
    implementation(libs.kotlinx.coroutines.core)

}

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}