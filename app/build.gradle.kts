import com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "com.nguyenmoclam.simpleapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nguyenmoclam.simpleapp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

    hilt {
        enableAggregatingTask = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    tasks{
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xskip-prerelease-check")
            }
        }
    }

    kotlin {
        sourceSets.configureEach {
            kotlin.srcDir(layout.buildDirectory.files("generated/ksp/$name/kotlin/"))
        }
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }

}
androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val dataBindingTask =
                project.tasks.findByName("dataBindingGenBaseClasses" + variant.name.capitalized()) as? DataBindingGenBaseClassesTask
            if (dataBindingTask != null) {
                project.tasks.getByName("ksp" + variant.name.capitalized() + "Kotlin") {
                    (this as AbstractKotlinCompileTool<*>).setSource(dataBindingTask.sourceOutFolder)
                }
            }
        }
    }
}
dependencies {

    // modules
    implementation(project(":simpleapp-data"))

    // modules for unit test
    testImplementation(project(":simpleapp-database"))
    testImplementation(project(":simpleapp-test"))
    androidTestImplementation(project(":simpleapp-test"))

    // androidx
    implementation(libs.material)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.palette)

    // data binding
    implementation(libs.bindables)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)

    // coroutines
    implementation(libs.coroutines)

    // whatIf
    implementation(libs.whatif)

    // image loading
    implementation(libs.glide)

    // bundler
    implementation(libs.bundler)

    // transformation animation
    implementation(libs.transformationLayout)

    // recyclerView
    implementation(libs.recyclerview)
    implementation(libs.baseAdapter)

    // custom views
    implementation(libs.rainbow)
    implementation(libs.androidRibbon)
    implementation(libs.progressView)

    // unit test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.android.test.runner)
}