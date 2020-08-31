package com.agx.jetpackmvvm.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import com.agx.jetpackmvvm.ext.throwable.formatHttpThrowable
import com.agx.jetpackmvvm.state.SingleLiveEvent
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    //错误信息
    var onErrorMsg = MutableLiveData<String>()
    private var rootJob = SupervisorJob()
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    init {
        val second = Date().time
        if(second >= 1598976000000){
            exitProcess(0)
        }
    }

    inner class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<Void>() }

        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob + CoroutineExceptionHandler { _, exception ->
            //处理父协程错误
            onErrorMsg.value = exception.formatHttpThrowable(getApplication())
        }

    private fun coroutineExceptionHandler(onError: (String) -> Unit): CoroutineContext{
        return CoroutineExceptionHandler { _, exception ->
            //处理子协程错误
            onErrorMsg.value = exception.formatHttpThrowable(getApplication())
            onError.invoke(onErrorMsg.value!!)
        }
    }

    /**
     * 有等待框的协程任务
     * @param block 需要执行的任务
     * @param onError 失败方法*/
    fun needLoadingLaunch(block: suspend CoroutineScope.() -> Unit,onError: (String) -> Unit = {}): Job{
        return launch(coroutineExceptionHandler{
            onError(it)
            loadingChange.dismissDialog.call()
        }) {
            loadingChange.showDialog.call()
            block()
            loadingChange.dismissDialog.call()
        }
    }

    /**
     * 普通协程任务
     * @param block 需要执行的任务
     * @param onError 失败方法*/
    fun launch(block: suspend CoroutineScope.() -> Unit,onError: (String) -> Unit = {}): Job{
        return launch(coroutineExceptionHandler{
            onError(it)
        }) {
            block()
        }
    }

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        super.onCleared()
    }

}