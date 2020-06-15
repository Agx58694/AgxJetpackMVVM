package com.agx.agxjetpackmvvmtest.di

import com.agx.agxjetpackmvvmtest.constant.BASE_URL
import com.agx.agxjetpackmvvmtest.http.ApiService
import com.agx.agxjetpackmvvmtest.http.RetrofitClient
import com.agx.agxjetpackmvvmtest.model.repository.LoginRepository
import com.agx.agxjetpackmvvmtest.ui.fragment.home.HomeViewModel
import com.agx.agxjetpackmvvmtest.ui.fragment.login.LoginViewModel
import com.agx.agxjetpackmvvmtest.ui.fragment.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel {
        LoginViewModel(
            get(),
            get()
        )
    }
    viewModel { HomeViewModel(get()) }
}

val repositoryModule = module {
    single { RetrofitClient.getService(ApiService::class.java, BASE_URL) }
    single { LoginRepository(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule)