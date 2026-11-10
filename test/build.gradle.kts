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
    implementation(projects.creator.api)
    implementation(projects.creator.frontend.yaml)
    implementation(projects.creator.backend.generator.manual)
    implementation(projects.creator.backend.generator.android.kaspresso)
    implementation(projects.creator.backend.integrator.files)
    implementation(projects.creator.parser.ll1)
    implementation(projects.creator.logger.terminal)

    implementation(libs.kotlin.stdlib)

    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(libs.kotlinx.coroutines.core)
}