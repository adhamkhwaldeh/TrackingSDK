plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    // ... existing plugins ...
    id("kotlin-kapt")
}

android {
    namespace = "com.kerberos.livetrackingsdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
//        compose = true
        aidl = true // Ensure this is true or remove the line (as true is default)
        buildConfig = true // Or remove this line
        dataBinding = true

        // for view binding:
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson)
    implementation("com.opencsv:opencsv:5.12.0")

    implementation("androidx.datastore:datastore:1.1.7")
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")

    kapt("androidx.room:room-compiler:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")

    implementation("androidx.paging:paging-runtime:3.3.2") // Latest stable
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")


}