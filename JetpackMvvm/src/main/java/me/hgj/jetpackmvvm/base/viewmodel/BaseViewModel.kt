package me.hgj.jetpackmvvm.base.viewmodel

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData
import me.hgj.jetpackmvvm.ext.throwable.formatHttpThrowable
import me.hgj.jetpackmvvm.ext.throwable.formatSystemThrowable
import me.hgj.jetpackmvvm.state.SingleLiveEvent
import retrofit2.HttpException
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
            onErrorMsg.value = when(exception){
                is HttpException -> exception.formatHttpThrowable(getApplication())
                else -> exception.formatSystemThrowable()
            }
        }

    private fun coroutineExceptionHandler(onError: (String) -> Unit): CoroutineContext{
        return CoroutineExceptionHandler { _, exception ->
            //处理子协程错误
            onErrorMsg.value = when(exception){
                is HttpException -> exception.formatHttpThrowable(getApplication())
                else -> exception.formatSystemThrowable()
            }
            onError.invoke(onErrorMsg.value)
        }
    }

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

    @CallSuper
    override fun onCleared() {
        rootJob.cancel()
        super.onCleared()
    }

}