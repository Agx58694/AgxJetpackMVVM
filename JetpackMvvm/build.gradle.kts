plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 19
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.dataBinding = true
}

afterEvaluate{
    publishing.publications {
        create("release",MavenPublication::class.java){
            from(components.getByName("release"))
            groupId = "com.github.Agx58694"
            artifactId = "AgxJetpackMVVM"
            version = "1.0.7-rc3"
        }
    }
}

dependencies {
    //https://developer.android.google.cn/jetpack/androidx/releases/lifecycle
    api("androidx.lifecycle:lifecycle-common-java8:2.5.0")
    // LiveData
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
    // ViewModel
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
    // Saved state module for ViewModel
    api("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.5.0")
    // Koin Android
    api("io.insert-koin:koin-android:3.2.0")
    //navigation
    api("androidx.navigation:navigation-fragment-ktx:2.5.0")
    api("androidx.navigation:navigation-ui-ktx:2.5.0")
    //retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    //okhttp3 log
    api("com.squareup.okhttp3:logging-interceptor:4.9.1")
    //Room
    api("androidx.room:room-runtime:2.4.2")
    // optional - Kotlin Extensions and Coroutines support for Room
    api("androidx.room:room-ktx:2.4.2")
    //startup
    api("androidx.startup:startup-runtime:1.1.1")
}