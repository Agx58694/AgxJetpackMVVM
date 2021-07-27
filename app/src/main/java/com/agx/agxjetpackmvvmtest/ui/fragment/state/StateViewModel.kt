package com.agx.agxjetpackmvvmtest.ui.fragment.state

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.agx.jetpackmvvm.CustomException
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StateViewModel(application: Application) : BaseViewModel(application) {
    val loadDataResult = MutableLiveData<String>()

    /**
     * 加载数据
     * @param type 1=加载成功 2=加载失败 3=加载空数据*/
    fun loadData(type: Int) = layoutDataLaunch(
        block = {
            delay(3000)
            val result = when(type){
                1 -> loadData1()
                2 -> loadData2()
                3 -> loadData3()
                else -> loadData1()
            }
            loadDataResult.value = result
        }
    )

    private suspend fun loadData1(): String = suspendCoroutine { result ->
        launch(
            block = {
                delay(500)
                //正常加载数据。正常返回
                result.resume("123")
            }
        )
    }

    private suspend fun loadData2(): String = suspendCoroutine { result ->
        launch(
            block = {
                delay(500)
                //抛出异常
                result.resumeWithException(CustomException("发生异常：123456"))
            }
        )
    }

    private suspend fun loadData3(): String = suspendCoroutine { result ->
        launch(
            block = {
                delay(500)
                //正常加载数据，返回空
                result.resume("")
            }
        )
    }
}