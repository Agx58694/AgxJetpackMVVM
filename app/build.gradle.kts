plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    kotlin("kapt")
}

android {
    namespace = "com.agx.jetpackmvvmtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.agx.jetpackmvvmtest"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":JetpackMvvm"))

    //https://github.com/CymChad/BaseRecyclerViewAdapterHelper
    implementation(libs.baserecyclerviewadapterhelper4)
    //状态界面管理库 https://github.com/KingJA/LoadSir
    implementation(libs.loadsir)
    //glide
    implementation(libs.glide)
    //屏幕适配
    implementation(libs.autosize)
    //util
    implementation(libs.utilcodex)
    //https://github.com/li-xiaojun/XPopup
    implementation(libs.xpopup)
    //https://github.com/airbnb/lottie-android
    implementation(libs.lottie)
    // 基础依赖包，必须要依赖 https://github.com/gyf-dev/ImmersionBar
    implementation(libs.immersionbar)
    // fragment快速实现（可选）https://github.com/gyf-dev/ImmersionBar
    implementation(libs.immersionbar.components)
    //QMUI  https://qmuiteam.com/android/get-started
    implementation(libs.qmui)
    //room
    ksp(libs.androidx.room.compiler)
    //DataStore
    implementation(libs.androidx.datastore.preferences)
    debugImplementation(libs.leakcanary.android)
    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
}