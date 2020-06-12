package com.agx.agxjetpackmvvm.model.repository

import com.agx.agxjetpackmvvm.http.ApiService
import kotlinx.coroutines.delay
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.unpacking

class LoginRepository(private val apiService: ApiService) {
    suspend fun getCode(success: (String) -> Unit,failure: (String) -> Unit = {}){
        apiService.getCode("15519122987").unpacking(
            success = { success.invoke("发送成功") },
            failure = { failure.invoke(it) }
        )
    }
}