package com.agx.agxjetpackmvvmtest.model.repository

import com.agx.agxjetpackmvvmtest.app.base.BaseRepository
import com.agx.agxjetpackmvvmtest.http.ApiService
import com.agx.jetpackmvvm.CustomException
import kotlinx.coroutines.delay
import java.lang.RuntimeException

class LoginRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getCode(): String? {
        //这里执行模拟网络请求成功测试
        return apiService.baiduApi()
    }
    
    suspend fun getCodeError1(success: (String) -> Unit = {}, failure: (String) -> Unit){
        //这里执行模拟网络请求失败测试
        delay(2000)
        //如果只是简单提示用户原因如：用户名错误请重新输入。这里抛出的错误会通过ViewModel.onErrorMsg发射出去
        throw CustomException("请求失败")
    }

    suspend fun getCodeError2(success: (String) -> Unit = {}, failure: (String) -> Unit){
        //这里执行模拟网络请求失败测试
        delay(2000)
        //如果这里失败ui层需要处理相应动作如：刷新获取数据，这里得结束刷新并显示刷新失败。
        failure.invoke("请求失败")
    }

    suspend fun getCodeError3(success: (String) -> Unit = {}, failure: (String) -> Unit){
        //这里执行模拟网络请求失败测试
        delay(2000)
        //如果这里发成了不可预估的错误 如：后台约定id不能为空，但是后台出错返回了给你，那么就可抛出内置异常类。然后通过获取VM的错误，可以写入日志也可进行其他操作
        //具体请查看默认ViewModel错误处理器
        throw RuntimeException("at LoginRepository.getCodeError3: id is null")
    }
}