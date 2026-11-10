plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(projects.api)
    api(projects.compiler.frontend.api)
    api(projects.compiler.logger.api)

    implementation(projects.compiler.ast)
    implementation(projects.compiler.grammar)
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