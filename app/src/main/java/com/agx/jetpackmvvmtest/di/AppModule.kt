package com.agx.jetpackmvvmtest.di

import androidx.room.Room
import com.agx.jetpackmvvmtest.constant.BASE_URL
import com.agx.jetpackmvvmtest.constant.ROOM_DATABASE_NAME
import com.agx.jetpackmvvmtest.db.UserDatabase
import com.agx.jetpackmvvmtest.http.ApiService
import com.agx.jetpackmvvmtest.http.RetrofitClient
import com.agx.jetpackmvvmtest.model.repository.LoginRepository
import com.agx.jetpackmvvmtest.model.repository.UserRepository
import com.agx.jetpackmvvmtest.ui.activity.DataStoreViewModel
import com.agx.jetpackmvvmtest.ui.fragment.db.DbViewModel
import com.agx.jetpackmvvmtest.ui.fragment.home.HomeViewModel
import com.agx.jetpackmvvmtest.ui.fragment.login.LoginViewModel
import com.agx.jetpackmvvmtest.ui.fragment.main.MainViewModel
import com.agx.jetpackmvvmtest.ui.fragment.shared.SharedElementTransitionVM
import com.agx.jetpackmvvmtest.ui.fragment.state.StateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DbViewModel)
    viewModelOf(::StateViewModel)
    viewModelOf(::SharedElementTransitionVM)
}

val repositoryModule = module {
    single { RetrofitClient.getService(ApiService::class.java, BASE_URL) }
    single {
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java, ROOM_DATABASE_NAME
        ).build()
    }
    singleOf(::LoginRepository)
    singleOf(::UserRepository)
    singleOf(::DataStoreViewModel)
}

val appModule = listOf(viewModelModule, repositoryModule)