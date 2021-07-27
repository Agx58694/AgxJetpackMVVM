package com.agx.agxjetpackmvvmtest.http

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("s")
    suspend fun baiduApi(@Query("wd") wd: String = "奥运会"): String?
}