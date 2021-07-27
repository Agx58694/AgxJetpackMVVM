package com.agx.jetpackmvvm.base.viewmodel

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.agx.jetpackmvvm.configure.loadingContent
import com.agx.jetpackmvvm.ext.formatThrowable
import com.agx.jetpackmvvm.ext.ifFalse
import com.agx.jetpackmvvm.ext.ifTrue
import com.agx.jetpackmvvm.network.manager.NetworkStateManager
import com.agx.jetpackmvvm.state.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    //错误信息
    var onErrorMsg = SingleLiveEvent<String>()
    private var rootJob = SupervisorJob()
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }
    val layoutDataChange: LayoutDataChange by lazy { LayoutDataChange() }
    val mContext: Context by lazy { getApplication<Application>() }

    class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<String>() }

        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

    class LayoutDataChange {
        //加载中
        val layoutDataLoading by lazy { SingleLiveEvent<Void>() }

        //加载失败
        val layoutDataError by lazy { SingleLiveEvent<String>() }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob + CoroutineExceptionHandler { _, exception ->
            //处理父协程错误
            onErrorMsg.value = exception.formatThrowable(getApplication())
        }

    fun coroutineExceptionHandler(
        isSendError: Boolean = false,
        onError: (String) -> Unit = {}
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
     * 带加载反馈的协程任务
     * isNetworkJob true 代表需要网络的协程工作
     * startLoading 反馈开始工作的函数
     * finishLoading 反馈结束工作的函数*/
    fun loadingBackLaunch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((String) -> Unit)? = null,
        isNetworkJob: Boolean = true,
        startLoading: (() -> Unit) = {},
        finishLoading: (() -> Unit) = {}
    ): Job {
        return launch(coroutineExceptionHandler(onError == null) {
            onError?.invoke(it)
        }) {
            isNetworkJob.ifTrue {
                //检查网络，没有网络则抛出异常
                NetworkStateManager.instance.mNetworkStateCallback.value?.isSuccess?.ifFalse {
                    //不处理，直接返回。该错误逻辑请重写onNetworkStateChanged处理
                    return@launch
                }
            }
            startLoading.invoke()
            block()
            finishLoading.invoke()
        }
    }

    /**
     * 带状态界面回调的协程任务
     * 界面初始化所需数据可全部放到这里，这里将发送加载中、加载完成、加载失败事件发送到对应的fragment或activity
     * isNetworkJob true 代表需要网络的协程工作*/
    fun layoutDataLaunch(
        block: suspend CoroutineScope.() -> Unit,
        isNetworkJob: Boolean = true,
    ): Job {
        return launch(coroutineExceptionHandler {
            layoutDataChange.layoutDataError.value = it
        }) {
            isNetworkJob.ifTrue {
                //检查网络，没有网络则抛出异常
                NetworkStateManager.instance.mNetworkStateCallback.value?.isSuccess?.ifFalse {
                    //不处理，直接返回。该错误逻辑请重写onNetworkStateChanged处理
                    return@launch
                }
            }
            layoutDataChange.layoutDataLoading.call()
            block()
        }
    }

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        super.onCleared()
    }

}