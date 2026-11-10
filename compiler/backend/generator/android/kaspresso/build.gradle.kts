plugins {
    alias(libs.plugins.kotlinJvm)
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":compiler:semantic"))
    implementation(project(":compiler:backend:generator:api"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    // Source: https://mvnrepository.com/artifact/com.squareup/kotlinpoet
    implementation(libs.kotlinpoet)
    // Source: https://mvnrepository.com/artifact/org.json/json
    implementation(libs.json)
}

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}