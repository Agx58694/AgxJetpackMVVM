package com.agx.agxjetpackmvvmtest.ui.fragment.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay

class MainViewModel(application: Application): BaseViewModel(application){
    var dataResult = MutableLiveData<Result<String>>()
    fun getData() = needLoadingLaunch(
        block = {
            //todo 代表耗时操作
            delay(2000)
            dataResult.value = Result.success("成功")
        },
        onError = { onErrorMsg.value = it }
    )
}