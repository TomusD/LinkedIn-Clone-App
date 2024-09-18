import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val ip: String = gradleLocalProperties(rootDir, providers).getProperty("ip") ?: ""
val port: String = gradleLocalProperties(rootDir, providers).getProperty("port") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.front"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.front"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ip", "$ip")
            buildConfigField("String", "port", "$port")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.java.websocket)

    implementation (libs.androidx.media3.exoplayer) // ExoPlayer
    implementation (libs.androidx.media3.ui)


    implementation("androidx.compose.material3:material3-window-size-class:1.0.0")
    implementation(libs.converter.gson.v250)

    // Coil for Compose
    implementation(libs.converter.scalars)
    implementation(libs.coil)
    implementation(libs.coil.compose.v270) // Use the latest version

    //Dagger - Hilt`
    implementation(libs.hilt.android.v244)
    implementation(libs.androidx.material3.android)
    kapt(libs.hilt.android.compiler.v244)


    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Dagger Android
    api(libs.dagger.android)
    api(libs.dagger.android.support)
    kapt(libs.dagger.android.processor)


    implementation(libs.androidx.hilt.navigation.compose)
//
//    implementation(libs.androidx.hilt.lifecycle.viewmodel)
//    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.ui)

    implementation(libs.androidx.runtime.livedata) // Check for the latest version
    annotationProcessor(libs.compiler)


//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.kotlinx.coroutines.android)


    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}