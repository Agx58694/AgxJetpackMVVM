package com.agx.agxjetpackmvvmtest.app

import android.app.Application
import android.content.Context
import com.agx.agxjetpackmvvmtest.di.appModule
import com.agx.jetpackmvvm.ext.throwable.setOnFormatThrowable
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
        setOnFormatThrowable { throwable, context ->
            return@setOnFormatThrowable "我是返回的自定义异常"
        }
    }
}