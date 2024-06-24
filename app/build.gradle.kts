import org.jetbrains.kotlin.diagnostics.reportOnDeclaration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
    id ("com.google.devtools.ksp")
    id ("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.fjr619.newsloc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fjr619.newsloc"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true
    includeSourceInformation = true
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
//    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
      implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.ui:ui:1.7.0-beta03")
    implementation("androidx.compose.animation:animation:1.7.0-beta03")

//    implementation("androidx.compose.foundation:foundation")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp ("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

//    //Kotlinx Serialization
//    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
//    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    //Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    //Datastore
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    //Paging 3
    implementation ("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation ("androidx.paging:paging-compose:3.3.0")

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

    implementation("androidx.biometric:biometric:1.1.0")

    implementation ("androidx.compose.material3.adaptive:adaptive:1.0.0-beta03")
    implementation ("androidx.compose.material3.adaptive:adaptive-layout:1.0.0-beta03")
    implementation ("androidx.compose.material3.adaptive:adaptive-navigation:1.0.0-beta03")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta03")
}