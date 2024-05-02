import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.snowball.memetory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.snowball.memetory"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", properties.getProperty("base_url"))
        buildConfigField("String", "S3ACCESSKEY", properties.getProperty("s3_access_key"))
        buildConfigField("String", "S3SECRETKEY", properties.getProperty("s3_secret_key"))
        buildConfigField("String", "S3BUCKET", properties.getProperty("s3_bucket"))
        buildConfigField("String", "GOOGLE_LOGIN_CLIENT_ID", properties.getProperty("google_login_client_id"))
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0-RC2")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.12")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:annotations:4.16.0")

    // cardview
    implementation("androidx.cardview:cardview:1.0.0")

    // jetpack navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // circleImageView : https://github.com/hdodenhof/CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // circleIndicator : https://github.com/ongakuer/CircleIndicator
    implementation("me.relex:circleindicator:2.1.6")

    // AWS S3: https://mvnrepository.com/artifact/com.amazonaws/aws-android-sdk-s3
    implementation("com.amazonaws:aws-android-sdk-mobile-client:2.75.0")
    implementation("com.amazonaws:aws-android-sdk-cognito:2.20.1")
    implementation("com.amazonaws:aws-android-sdk-s3:2.75.0")

    // Google Login
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.gms:google-services:4.3.15")
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation("com.google.firebase:firebase-bom:32.0.0")
}