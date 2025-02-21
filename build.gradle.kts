import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.spotless)
}

private typealias AndroidExtension = com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>

private val Project.androidExtension: AndroidExtension
    get() = extensions.getByType(com.android.build.api.dsl.CommonExtension::class.java)

private fun Project.android(configure: AndroidExtension.() -> Unit) {
    plugins.withType<com.android.build.gradle.BasePlugin>().configureEach {
        androidExtension.configure()
    }
}

private val targetSdkVersion = libs.versions.targetSdk.get().toInt()
private val bytecodeVersion = JavaVersion.toVersion(libs.versions.jvmBytecode.get())

subprojects {
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    // Common config
    android {
        defaultConfig {
            vectorDrawables.useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = bytecodeVersion
            targetCompatibility = bytecodeVersion
        }

        lint {
            abortOnError = false
        }
    }

    plugins.withType<com.android.build.gradle.AppPlugin>().configureEach {
        extensions.configure<com.android.build.api.dsl.ApplicationExtension> {
            defaultConfig {
                targetSdk = targetSdkVersion
            }
        }
    }

    plugins.withType<com.android.build.gradle.TestPlugin>().configureEach {
        extensions.configure<com.android.build.api.dsl.TestExtension> {
            defaultConfig {
                targetSdk = targetSdkVersion
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = bytecodeVersion.toString()
        kotlinOptions.freeCompilerArgs += listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlin.time.ExperimentalTime"
        )
    }

    extensions.configure<SpotlessExtension> {
        val buildDirectory = layout.buildDirectory.asFileTree
        kotlin {
            target("**/*.kt")
            targetExclude(buildDirectory)
            ktlint().editorConfigOverride(
                mapOf(
                    "indent_size" to "2",
                    "continuation_indent_size" to "2",
                )
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude(buildDirectory)
        }

        format("xml") {
            target("**/*.xml")
            targetExclude(buildDirectory)
        }
    }

}



