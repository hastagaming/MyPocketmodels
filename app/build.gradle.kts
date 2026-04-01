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

        // --- LOGIKA TOKEN RAHASIA (JANGAN DIHAPUS) ---
        val hfToken = project.rootProject.file("local.properties").let { file ->
            if (file.exists()) {
                val properties = java.util.Properties()
                properties.load(file.inputStream())
                properties.getProperty("HF_TOKEN") ?: ""
            } else {
                System.getenv("HF_TOKEN") ?: ""
            }
        }
        buildConfigField("String", "HF_TOKEN", "\"$hfToken\"")
        // ---------------------------------------------
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // PERBAIKAN: Harus di dalam composeOptions, bukan compileOptions
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
    // UI (Compose) - Disarankan pakai BOM agar versi seragam
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
}
