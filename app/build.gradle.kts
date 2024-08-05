import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.baselineprofile")
}
val javaVersion = JavaVersion.toVersion(libs.versions.jvmTarget.get())

android {
    namespace = "com.jobik.gameoflife"
    compileSdk = libs.versions.androidCompileSdk.get().toIntOrNull()

    defaultConfig {
        applicationId = "com.jobik.gameoflife"
        minSdk = libs.versions.androidMinSdk.get().toIntOrNull()
        targetSdk = libs.versions.androidTargetSdk.get().toIntOrNull()
        versionCode = libs.versions.versionCode.get().toIntOrNull()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        android.buildFeatures.buildConfig = true

        archivesName.set("shkiper-$versionName")
    }

    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("long", "VERSION_CODE", "${defaultConfig.versionCode}")
            buildConfigField("String", "VERSION_NAME", "\"${defaultConfig.versionName}\"")
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
        debug {
            buildConfigField("long", "VERSION_CODE", "${defaultConfig.versionCode}")
            buildConfigField("String", "VERSION_NAME", "\"${defaultConfig.versionName}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.profileinstaller)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    "baselineProfile"(project(":baselineprofile"))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.material.icons.extended)

    // Compose navigation
    implementation(libs.androidx.navigation.compose)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Compose collapsing toolbar (Official latest update in 2022)
    // implementation("me.onebone:toolbar-compose:2.3.5")
    // Compose collapsing toolbar (nonofficial)
    implementation(libs.composecollapsingtoolbar)

    // Jetpack lifecycle observer
    implementation(libs.androidx.lifecycle.runtime.compose)



}