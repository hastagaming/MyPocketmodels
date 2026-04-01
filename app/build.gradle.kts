import java.util.Properties
import java.io.File

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
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

        // --- LOGIKA TOKEN RAHASIA ---
        // Kita gunakan cara yang lebih eksplisit agar Gradle tidak bingung
        val localPropsFile = project.rootProject.file("local.properties")
        val properties = Properties()
        
        val hfToken = if (localPropsFile.exists()) {
            localPropsFile.inputStream().use { properties.load(it) }
            properties.getProperty("HF_TOKEN") ?: ""
        } else {
            System.getenv("HF_TOKEN") ?: ""
        }
        
        buildConfigField("String", "HF_TOKEN", "\"$hfToken\"")
        // ----------------------------
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    // UI (Compose)
    val composeBom = platform("androidx.compose:compose-bom:2024.02.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Memory (Room Database)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // TAMBAHKAN 3 BARIS INI: Library untuk Activity, ViewModel, dan Lifecycle
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}
