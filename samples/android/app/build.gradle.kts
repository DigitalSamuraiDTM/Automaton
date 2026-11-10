plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)

}

val projectVersion: String by rootProject.extra

android {
    namespace = "com.digitalsamurai.automaton.sample"
    compileSdk = 37

    with(buildFeatures) {
        this.buildConfig = true
    }
    defaultConfig {
        applicationId = "com.digitalsamurai.automaton.sample"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = projectVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "PROJECT_VERSION", "\"$projectVersion\"")
    }

    buildTypes {
        create("automaton") {
            initWith(getByName("debug"))
            matchingFallbacks.add("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

androidComponents {
    beforeVariants(selector().withBuildType("automaton")) { variant ->
        variant.enableAndroidTest = true
    }
}

dependencies {

    implementation(project(":math"))
    implementation(project(":algos"))
    implementation(project(":utils"))
    implementation(project(":network:auth"))
    implementation(project(":network:frontback"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material.icons)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // implement compose (compost)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.fragment.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.drawerlayout)
    debugImplementation(libs.androidx.ui.tooling)

    // TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.compose)
    androidTestImplementation(libs.kakaocup)
    androidTestImplementation(libs.kakaocup.compose)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.okhttp.webmockserver)

}