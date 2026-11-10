plugins {
    alias(libs.plugins.kotlinJvm)
}


repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(project(":compiler:compiler"))
    implementation(project(":compiler:frontend:yaml"))
    implementation(project(":compiler:backend:generator:manual"))
    implementation(project(":compiler:backend:generator:android:kaspresso"))
    implementation(project(":compiler:backend:integrator:files"))
    implementation(project(":compiler:parser:ll1"))
    implementation(project(":compiler:logger:terminal"))

    implementation(libs.kotlin.stdlib)

    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(libs.kotlinx.coroutines.core)
}