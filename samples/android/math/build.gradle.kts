plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}
val projectVersion: String by rootProject.extra
group = "com.digitalsamurai.math"
version = projectVersion

java {

}
kotlin {
    jvmToolchain(25)
}
dependencies {
    testImplementation(libs.junit)
}
