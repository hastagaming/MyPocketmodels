plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // Pakai KSP di sini
}

android {
    namespace = "com.hastagaming.mypocketmodels"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hastagaming.mypocketmodels"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        // Memaksa Java 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    kotlinOptions {
        // Memaksa Kotlin menggunakan JVM 17
        jvmTarget = "17"
    }

    // Tambahkan ini di dalam blok android { ... } agar lebih aman
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    // UI (Compose)
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    
    // Memory (Room Database) pakai KSP
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion") // Ganti kapt jadi ksp

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
