package com.agx.agxjetpackmvvmtest.http

data class ResponseWrapper<T>(
    var code: Int,
    var success: Boolean,
    var data: T?,
    var message: String
)