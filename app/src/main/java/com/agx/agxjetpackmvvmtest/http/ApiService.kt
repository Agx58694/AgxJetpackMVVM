package com.agx.agxjetpackmvvmtest.http

import com.agx.jetpackmvvm.network.bean.ResponseWrapper
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by luyao
 * on 2018/3/13 14:33
 */
interface ApiService {
    /**
     * 获取验证码 */
    @FormUrlEncoded
    @POST("m/driver/getCheckcodeMsg")
    suspend fun getCode(@Field("phoneNums") phone: String): ResponseWrapper<String>
}