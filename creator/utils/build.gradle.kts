plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(projects.api)
    api(projects.creator.frontend.api)
    api(projects.creator.logger.api)

    implementation(projects.creator.ast)
    implementation(projects.creator.grammar)
    implementation(libs.kotlin.stdlib)
    implementation(libs.json)
    implementation(libs.kotlin.reflect)

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}