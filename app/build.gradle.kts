plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ailanguagetutor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ailanguagetutor"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Add API key to BuildConfig
        buildConfigField("String", "apiKey", "\"AIzaSyA2eF-FQ2QxLa6tRgQILExWCu9rlQmOW0g\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Add Gemini SDK
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.guava:guava:31.0.1-android")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}