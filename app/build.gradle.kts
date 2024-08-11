/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "pt.rvcoding.cvnotes"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pt.rvcoding.cvnotes"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.1.0"

        testInstrumentationRunner = "pt.rvcoding.cvnotes.HiltTestRunner"
        vectorDrawables.useSupportLibrary = true
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    val properties = mutableMapOf<String, String>()
    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.jks")
            storePassword = "my4PP!D3bUg"
            keyAlias = "debug"
            keyPassword = "my4PP!D3bUg"
        }

        val keyPropertiesFile = file("../../rvcoding/release.properties")
        if (keyPropertiesFile.canRead()) {
            println("Release build set with release keystore")

            val properties = Properties()
            properties.load(FileInputStream(keyPropertiesFile))
            create("release") {
                storeFile = file("../../rvcoding/release.jks")
                storePassword = properties["storePassword"].toString()
                keyAlias = properties["keyAlias"].toString()
                keyPassword = properties["keyPassword"].toString()
            }
        } else {
            println("Release build set with debug keystore")

            create("release") {
                storeFile = file("../debug.jks")
                storePassword = "my4PP!D3bUg"
                keyAlias = "debug"
                keyPassword = "my4PP!D3bUg"
            }
        }

        buildTypes {
            getByName("debug") {
                isDebuggable = true
                isMinifyEnabled = false
                isDefault = true

                signingConfig = signingConfigs.getByName("debug")

                applicationIdSuffix = ".debug"
                resValue("string", "app_name", "CVNotes Debug")
            }
            getByName("release") {
                isDebuggable = false
                isMinifyEnabled = true
                isDefault = false

                signingConfig = signingConfigs.getByName("release")
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlin {
            jvmToolchain(17)
        }
        buildFeatures {
            compose = true
        }
        packaging.resources {
            // Multiple dependency bring these files in. Exclude them to enable
            // our test APK to build (has no effect on our AARs)
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
        }
    }
    composeCompiler {
        enableStrongSkippingMode = true
    }
    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.kotlin.reflect)

        // Compose UI
        platform(libs.androidx.compose.bom).let { composeBom ->
            implementation(composeBom)
            androidTestImplementation(composeBom)
        }
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.tooling.preview)
        debugImplementation(libs.androidx.compose.ui.test.manifest)
        implementation(libs.androidx.compose.material.iconsExtended)
        implementation(libs.androidx.compose.material3)
        implementation(libs.lottie.compose)
        implementation(libs.accompanist.systemuicontroller)

        // Compose + Lifecycle
        implementation(libs.androidx.lifecycle.runtime)
        implementation(libs.androidx.lifecycle.runtime.compose)
        implementation(libs.androidx.lifecycle.viewModelCompose)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.hilt.navigation.compose)

        // Coroutines
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)
        testImplementation(libs.kotlinx.coroutines.test)

        // Dagger - Hilt
        implementation(libs.hilt.android)
        ksp(libs.hilt.android.compiler)
        ksp(libs.hilt.compiler)
        testImplementation(libs.hilt.android.testing)
        androidTestImplementation(libs.hilt.android.testing)
        kspTest(libs.hilt.android.compiler)
        kspAndroidTest(libs.hilt.android.compiler)

        // Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx) // Kotlin Extensions and Coroutines support for Room
        ksp(libs.androidx.room.compiler)

        // Firebase
        implementation(platform(libs.firebase.bom))
        implementation(libs.firebase.auth.ktx)
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.crashlytics.ktx)
        implementation(libs.android.google.services.auth)
    }

    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}



//    // Unit testing
//    testImplementation "androidx.test:core:1.6.1"
//    testImplementation "androidx.arch.core:core-testing:2.2.0"
//    testImplementation "junit:junit:4.13.2"
//    testImplementation "org.junit.jupiter:junit-jupiter-api:5.9.3"
//    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:5.9.3"
//    testImplementation "org.junit.jupiter:junit-jupiter-params:5.9.3"
//    testImplementation "app.cash.turbine:turbine:0.7.0"
//    testImplementation "com.google.truth:truth:1.1.3"
//    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
//    testImplementation "com.squareup.okhttp3:mockwebserver:4.9.1"
//    testImplementation "io.mockk:mockk:1.12.5"
//    testImplementation "com.willowtreeapps.assertk:assertk:0.26.1"
//
//    // Instrumented testing
//    androidTestImplementation 'androidx.test:core-ktx:1.6.1'
//    androidTestImplementation "junit:junit:4.13.2"
//    androidTestImplementation "androidx.arch.core:core-testing:2.2.0"
//    androidTestImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
//    androidTestImplementation "com.google.truth:truth:1.1.3"
//    androidTestImplementation "com.squareup.okhttp3:mockwebserver:4.9.1"
//    androidTestImplementation "io.mockk:mockk-android:1.12.5"
//    androidTestImplementation 'androidx.test:runner:1.6.1'
//    androidTestImplementation 'androidx.test.ext:junit:1.2.1'
//    androidTestImplementation "com.google.dagger:hilt-android-testing:$hilt_version"
//    kspAndroidTest "com.google.dagger:hilt-android-compiler:$hilt_version"
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
//}
