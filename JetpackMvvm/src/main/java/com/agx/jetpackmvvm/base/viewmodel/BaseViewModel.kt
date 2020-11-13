package com.agx.jetpackmvvm.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.agx.jetpackmvvm.configure.loadingContent
import kotlinx.coroutines.*
import com.agx.jetpackmvvm.ext.throwable.formatThrowable
import com.agx.jetpackmvvm.ext.util.ifTrue
import com.agx.jetpackmvvm.state.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    //错误信息
    var onErrorMsg = SingleLiveEvent<String>()
    private var rootJob = SupervisorJob()
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<String>() }

        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob + CoroutineExceptionHandler { _, exception ->
            //处理父协程错误
            onErrorMsg.value = exception.formatThrowable(getApplication())
        }

    private fun coroutineExceptionHandler(
        isSendError: Boolean,
        onError: (String) -> Unit
    ): CoroutineContext {
        return CoroutineExceptionHandler { _, exception ->
            exception.formatThrowable(getApplication()).let {
                //处理子协程错误
                isSendError.ifTrue {
                    onErrorMsg.value = it
                }
                onError.invoke(it)
            }
        }
    }

    /**
     * 有等待框的协程任务
     * @param block 需要执行的任务
     * @param onError 失败方法*/
    fun needLoadingLaunch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((String) -> Unit)? = null,
        loadingTitle: String = loadingContent
    ): Job {
        return launch(coroutineExceptionHandler(onError == null) {
            onError?.invoke(it)
            loadingChange.dismissDialog.call()
        }) {
            loadingChange.showDialog.value = loadingTitle
            block()
            loadingChange.dismissDialog.call()
        }
    }

    /**
     * 普通协程任务
     * @param block 需要执行的任务
     * @param onError 失败方法*/
    fun launch(block: suspend CoroutineScope.() -> Unit, onError: ((String) -> Unit)? = null): Job {
        return launch(coroutineExceptionHandler(onError == null) {
            onError?.invoke(it)
        }) {
            block()
        }
    }

    /**
     * 带加载反馈的协程任务*/
    fun loadingBackLaunch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((String) -> Unit)? = null,
        startLoading: (() -> Unit) = {},
        finishLoading: (() -> Unit) = {}
    ): Job {
        return launch(coroutineExceptionHandler(onError == null) {
            onError?.invoke(it)
        }) {
            startLoading.invoke()
            block()
            finishLoading.invoke()
        }
    }

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        super.onCleared()
    }

}