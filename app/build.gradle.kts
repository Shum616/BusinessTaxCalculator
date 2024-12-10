import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.businesstaxcalculator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.businesstaxcalculator"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
    }
    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/NOTICE.md")
    }
}

dependencies {
    implementation(libs.androidx.databinding.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.material)
    // Use to implement support for wear tiles
    implementation(libs.androidx.tiles)

    // Use to utilize standard components and layouts in your tiles
    implementation(libs.androidx.protolayout)

    // Use to utilize components and layouts with Material Design in your tiles
    implementation(libs.androidx.protolayout.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)

// Views/Fragments integration
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava3)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    kapt (libs.androidx.room.compiler)
    implementation ("com.google.android.material:material:1.12.0")

}
kapt {
    correctErrorTypes = true
}