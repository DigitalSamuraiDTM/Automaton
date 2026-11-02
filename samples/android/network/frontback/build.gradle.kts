plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
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
    api(project(":core:network"))
    implementation(libs.hilt.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.android.compiler)
}
