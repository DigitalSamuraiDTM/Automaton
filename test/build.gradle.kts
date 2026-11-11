plugins {
    alias(libs.plugins.kotlinJvm)
    id("application")
}

application {
    mainClass.set("com.digitalsamurai.automaton.test.MainKt")
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
    implementation(projects.grabber.api)
    implementation(projects.grabber.frontend.openapiFile)

    implementation(libs.kotlin.stdlib)

    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(libs.kotlinx.coroutines.core)
}