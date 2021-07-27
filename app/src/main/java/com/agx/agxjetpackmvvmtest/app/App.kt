package com.agx.agxjetpackmvvmtest.app

import android.app.Application
import android.content.Context
import com.agx.agxjetpackmvvmtest.di.appModule
import com.agx.agxjetpackmvvmtest.ui.custom.EmptyCallback
import com.agx.agxjetpackmvvmtest.ui.custom.ErrorCallback
import com.agx.agxjetpackmvvmtest.ui.custom.LoadingCallback
import com.agx.agxjetpackmvvmtest.ui.custom.NoNetworkCallback
import com.kingja.loadsir.core.LoadSir
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

/**
 * Created by luyao
 * on 2018/3/13 13:35
 */
class App : Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        initLoadSir()
    }

    private fun initLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(NoNetworkCallback())
            .commit()
    }
}