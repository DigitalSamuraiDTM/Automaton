plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(project(":compiler:frontend:api"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":compiler:grammar"))

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