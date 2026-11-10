plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(projects.creator.frontend.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.creator.grammar)

    // Source: https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation(libs.snakeyaml)
    implementation(libs.json)

}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}