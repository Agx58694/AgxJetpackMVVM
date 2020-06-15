package com.agx.agxjetpackmvvmtest.model.repository

import com.agx.agxjetpackmvvm.ext.util.unpacking
import com.agx.agxjetpackmvvmtest.http.ApiService

class LoginRepository(private val apiService: ApiService) {
    suspend fun getCode(success: (String) -> Unit,failure: (String) -> Unit = {}){
        apiService.getCode("15519122987").unpacking(
            success = { success.invoke("发送成功") },
            failure = { failure.invoke(it) }
        )
    }
}