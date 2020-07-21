package com.agx.jetpackmvvm.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import com.agx.jetpackmvvm.callback.livedata.StringLiveData
import com.agx.jetpackmvvm.ext.throwable.formatHttpThrowable
import com.agx.jetpackmvvm.state.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    //错误信息
    var onErrorMsg = StringLiveData()
    private var rootJob = SupervisorJob()
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

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
            onError.invoke(exception.formatHttpThrowable(getApplication()))
        }
    }

    fun needLoadingLaunch(block: suspend CoroutineScope.() -> Unit,onError: (String) -> Unit = {}): Job{
        return launch(coroutineExceptionHandler{
            onError(it)
            loadingChange.dismissDialog.call()
        }) {
            loadingChange.showDialog.call()
            block()
            //这里不加延时的话block里面没有耗时操作会导致等待框销毁不了，todo 需要优化
            delay(1)
            loadingChange.dismissDialog.call()
        }
    }

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        super.onCleared()
    }

}