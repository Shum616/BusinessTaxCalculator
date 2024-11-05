plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.businesstaxcalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.businesstaxcalculator"
        minSdk = 28
        targetSdk = 34
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
    implementation (libs.material.v140)
    implementation(libs.androidx.fragment.ktx)


}