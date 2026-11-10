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
    implementation(projects.compiler.compiler)
    implementation(projects.compiler.frontend.yaml)
    implementation(projects.compiler.backend.generator.manual)
    implementation(projects.compiler.backend.generator.android.kaspresso)
    implementation(projects.compiler.backend.integrator.files)
    implementation(projects.compiler.parser.ll1)
    implementation(projects.compiler.logger.terminal)

    implementation(libs.kotlin.stdlib)

    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(libs.kotlinx.coroutines.core)
}