package com.agx.agxjetpackmvvmtest.app

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.agx.agxjetpackmvvmtest.di.appModule
import com.agx.agxjetpackmvvmtest.ui.custom.*
import com.agx.agxjetpackmvvmtest.work.AgxWorker
import com.agx.jetpackmvvm.configure.loadingContent
import com.agx.jetpackmvvm.ext.setOnAppThrowableListener
import com.agx.jetpackmvvm.ext.setOnOtherThrowableListener
import com.kingja.loadsir.core.LoadSir
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        loadingContent = "加载中"
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        initWork()
        initListener()
        initLoadSir()
    }

    private fun initListener(){
        //处理viewModel原始错误，所以viewModel的原始错误都会到这里，开发者可记录日子或上传服务器记录
        setOnAppThrowableListener {
            Toast.makeText(this,"原始错误：${it.message.toString()}", Toast.LENGTH_LONG).show()
            it.addSuppressed(Throwable().apply {
                message
            })
        }
        setOnOtherThrowableListener {
            when(it){
                is SQLiteConstraintException -> "数据库错误"
                else -> null
            }
        }
    }

    private fun initLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
            .addCallback(NoNetworkCallback())
            .addCallback(TimeoutCallback())
            .commit()
    }

    private fun initWork(){
        val agxWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<AgxWorker>()
                .build()
        WorkManager
            .getInstance(this)
            .enqueue(agxWorkRequest)

    }
}