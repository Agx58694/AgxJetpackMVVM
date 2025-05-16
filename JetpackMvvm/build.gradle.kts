plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    compileSdk = 35
    defaultConfig {
        minSdk = 19
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures.dataBinding = true
    namespace = "com.agx.jetpackmvvm"
}

afterEvaluate {
    publishing.publications {
        create("release", MavenPublication::class.java) {
            from(components.getByName("release"))
            groupId = "com.github.Agx58694"
            artifactId = "AgxJetpackMVVM"
            version = "1.0.7-rc4"
        }
    }
}

dependencies {
    // LiveData
    api(libs.androidx.lifecycle.livedata.ktx)
    // ViewModel
    api(libs.androidx.lifecycle.viewmodel.ktx)
    // Saved state module for ViewModel
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    // Koin Android
    api(libs.koin.android)
    //navigation
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    //retrofit
    api(libs.retrofit)
    api(libs.converter.gson)
    //okhttp3 log
    api(libs.logging.interceptor)
    //Room
    api(libs.androidx.room.runtime)
    // optional - Kotlin Extensions and Coroutines support for Room
    api(libs.androidx.room.ktx)
    //startup
    api(libs.androidx.startup.runtime)
}