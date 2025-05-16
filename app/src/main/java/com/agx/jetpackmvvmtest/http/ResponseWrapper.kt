package com.agx.jetpackmvvmtest.http

data class ResponseWrapper<T>(
    var code: Int,
    var data: T?,
    var msg: String
)