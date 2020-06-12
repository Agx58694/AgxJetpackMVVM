package com.agx.agxjetpackmvvm.di

import com.agx.agxjetpackmvvm.constant.BASE_URL
import com.agx.agxjetpackmvvm.http.ApiService
import com.agx.agxjetpackmvvm.http.RetrofitClient
import com.agx.agxjetpackmvvm.model.repository.LoginRepository
import com.agx.agxjetpackmvvm.ui.fragment.home.HomeViewModel
import com.agx.agxjetpackmvvm.ui.fragment.login.LoginViewModel
import com.agx.agxjetpackmvvm.ui.fragment.main.MainViewModel
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