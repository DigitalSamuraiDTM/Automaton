plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.digitalsamurai.automaton.sample.network.frontback"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        create("automaton") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
        }
    }

    sourceSets {
        getByName("debug").java.srcDir("src/standard/java")
        getByName("debug").kotlin.srcDir("src/standard/java")
        getByName("release").java.srcDir("src/standard/java")
        getByName("release").kotlin.srcDir("src/standard/java")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}

dependencies {
    api(projects.core.network)
    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.android.compiler)
}
