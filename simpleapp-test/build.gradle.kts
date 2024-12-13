plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nguyenmoclam.simpleapp_test"
//    compileSdk = 34
//
//    defaultConfig {
//        minSdk = 24
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//    lint {
//        abortOnError = false
//    }
//
//    kotlinOptions {
//        jvmTarget = "11"
//    }
}

dependencies {
    implementation(project(":simpleapp-model"))
    implementation(libs.coroutines)
    implementation(libs.coroutines.test)

//    // unit test
//    testImplementation(libs.junit)
//    testImplementation(libs.turbine)
//    testImplementation(libs.androidx.test.core)
//    testImplementation(libs.mockito.core)
//    testImplementation(libs.mockito.kotlin)
//    androidTestImplementation(libs.truth)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso)
//    androidTestImplementation(libs.android.test.runner)

    implementation(libs.junit)
}