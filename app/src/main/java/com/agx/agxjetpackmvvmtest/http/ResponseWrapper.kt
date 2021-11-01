package com.agx.agxjetpackmvvmtest.http

data class ResponseWrapper<T>(
    var code: Int,
    var data: T?,
    var msg: String
)