plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    implementation(libs.kotlinx.coroutines.core)

    api(projects.api)
    api(projects.compiler.backend.integrator.api)
    api(projects.compiler.logger.api)

    implementation(projects.compiler.ast)
    implementation(projects.compiler.grammar)
    implementation(libs.kotlin.stdlib)
    implementation(libs.json)

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}