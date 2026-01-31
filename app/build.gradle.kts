import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.apollographql.apollo")
}

val localProps = Properties()
val localPropsFile = File(rootProject.rootDir, "keys.properties")
if (localPropsFile.exists() && localPropsFile.isFile) {
    localPropsFile.inputStream().use {stream ->
        localProps.load(stream)
    }
}

android {
    namespace = "com.example.coffeescout"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.coffeescout"
        minSdk = 28
        targetSdk = 33
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
            buildConfigField("String", "API_KEY", localProps.getProperty("API_KEY"))
        }

        debug {
            buildConfigField("String", "API_KEY", localProps.getProperty("API_KEY"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

apollo {
    service("service") {
        packageName.set("com.example.coffeescout")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    val lifecycleVersion = "2.10.0"

    val composeBom = platform("androidx.compose:compose-bom:2025.12.00")
    implementation(composeBom)

    implementation("androidx.compose.ui:ui-android")
    implementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.paging:paging-compose")

    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("androidx.activity:activity-compose:1.12.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.paging:paging-runtime:3.3.6")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.apollographql.apollo:apollo-runtime:4.4.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
