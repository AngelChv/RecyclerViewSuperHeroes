plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Navegación jetpack
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"

    // Safe-Args
    // Tambien hay que poner un plugin en el fichero del mismo nombre que este pero del project
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.recyclerviewsuperheroes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recyclerviewsuperheroes"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.github.glide)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navegación jetpack
    val navVersion = "2.8.6"

    // Jetpack Compose integration
    //implementation("androidx.navigation:navigation-compose:$navVersion")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Feature module support for Fragments
    //implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // Testing Navigation
    //androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // JSON serialization library, works with the Kotlin serialization plugin
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}