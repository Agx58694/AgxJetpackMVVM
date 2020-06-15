package com.agx.jetpackmvvm.network.bean

data class ResponseWrapper<T>(var code: Int, var success: Boolean, var data: T?, var message: String)