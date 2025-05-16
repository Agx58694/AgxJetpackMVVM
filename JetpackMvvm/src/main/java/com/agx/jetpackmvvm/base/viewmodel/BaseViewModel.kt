package com.agx.jetpackmvvm.base.viewmodel

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.agx.jetpackmvvm.configure.loadingContent
import com.agx.jetpackmvvm.ext.formatThrowable
import com.agx.jetpackmvvm.startup.ConnectionInitializer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    //错误信息
    var onErrorMsg = MutableSharedFlow<String?>(extraBufferCapacity = 1)
    private var rootJob = SupervisorJob()
    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }
    val layoutDataChange: LayoutDataChange by lazy { LayoutDataChange() }
    val mContext: Context by lazy { getApplication() }

    class UiLoadingChange {
        //显示加载框
        val showDialog by lazy { MutableSharedFlow<String>(extraBufferCapacity = 1) }

        //隐藏
        val dismissDialog by lazy { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }
    }

    class LayoutDataChange {
        //加载中
        val layoutDataLoading by lazy { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

        //加载失败
        val layoutDataError by lazy { MutableSharedFlow<String?>(extraBufferCapacity = 1) }

        //加载超时
        val layoutDataTimeout by lazy { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob + CoroutineExceptionHandler { _, exception ->
            //处理父协程错误
            onErrorMsg.tryEmit(exception.formatThrowable(getApplication()))
        }

    fun coroutineExceptionHandler(
        isSendError: Boolean = false,
        onError: (String?) -> Unit = {},
        onException: (Throwable, String?) -> Unit = { _, _ -> }
    ): CoroutineContext {
        return CoroutineExceptionHandler { _, exception ->
            exception.formatThrowable(getApplication()).let {
                onException.invoke(exception, it)
                //处理子协程错误
                if (isSendError) {
                    onErrorMsg.tryEmit(it)
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
        onError: ((String?) -> Unit)? = null,
        loadingTitle: String = loadingContent
    ): Job {
        return launch(
            coroutineExceptionHandler(
                isSendError = onError == null,
                onError = {
                    onError?.invoke(it)
                    loadingChange.dismissDialog.tryEmit(Unit)
                }
            )) {
            loadingChange.showDialog.tryEmit(loadingTitle)
            block()
            loadingChange.dismissDialog.tryEmit(Unit)
        }
    }

    /**
     * 普通协程任务
     * @param block 需要执行的任务
     * @param onError 失败方法*/
    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((String?) -> Unit)? = null
    ): Job {
        return launch(
            coroutineExceptionHandler(
                isSendError = onError == null,
                onError = {
                    onError?.invoke(it)
                }
            )) {
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
        onError: ((String?) -> Unit)? = null,
        isNetworkJob: Boolean = true,
        startLoading: (() -> Unit) = {},
        finishLoading: (() -> Unit) = {}
    ): Job {
        return launch(
            coroutineExceptionHandler(
                isSendError = onError == null,
                onError = {
                    onError?.invoke(it)
                }
            )) {
            if (isNetworkJob) {
                //检查网络，没有网络则抛出异常
                if (ConnectionInitializer.connectionLiveData.value == false) {
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
        return launch(
            coroutineExceptionHandler(
                onException = { throwable, error ->
                    if (throwable is HttpException) {
                        if (throwable.code() == 408) {
                            layoutDataChange.layoutDataTimeout.tryEmit(Unit)
                        }
                    } else if (throwable is TimeoutException || throwable is TimeoutCancellationException) {
                        layoutDataChange.layoutDataTimeout.tryEmit(Unit)
                    } else {
                        layoutDataChange.layoutDataError.tryEmit(error)
                    }
                }
            )) {
            if (isNetworkJob) {
                //检查网络，没有网络则抛出异常
                if (ConnectionInitializer.connectionLiveData.value == false) {
                    //不处理，直接返回。该错误逻辑请重写onNetworkStateChanged处理
                    return@launch
                }
            }
            layoutDataChange.layoutDataLoading.tryEmit(Unit)
            block()
        }
    }

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        this.cancel()
        super.onCleared()
    }

}