package com.agx.jetpackmvvm.ext.throwable

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.agx.jetpackmvvm.BuildConfig
import com.agx.jetpackmvvm.ext.util.ifTrue
import com.agx.jetpackmvvm.network.ApiErrorModel
import com.agx.jetpackmvvm.network.ApiErrorType
import com.agx.jetpackmvvm.network.ApiErrorType.*
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 系统原始错误*/
private var onAppThrowableListener: (String) -> Unit = {}

fun setOnAppThrowableListener(it: (String) -> Unit){
    onAppThrowableListener = it
}

/**
 * 系统错误格式化扩展函数
 * 将异常处理成用户可识别
 * 把异常发送到服务器记录*/
private fun Throwable.formatSystemThrowable(): ApiErrorType {
    onAppThrowableListener.invoke(this.message.toString())
    //未预见的系统错误，只能统一格式化，该函数主要目的为上传原始错误到服务器记录
    return SYSTEM_ERROR
}

/**
 * http错误格式化扩展函数*/
fun Throwable.formatHttpThrowable(context: Context): String {
    if (this is HttpException) {
        val apiErrorModel: ApiErrorModel = when (this.code()) {
            INTERNAL_SERVER_ERROR.code ->
                INTERNAL_SERVER_ERROR.getApiErrorModel(context)
            BAD_GATEWAY.code ->
                BAD_GATEWAY.getApiErrorModel(context)
            NOT_FOUND.code ->
                NOT_FOUND.getApiErrorModel(context)
            CONNECTION_TIMEOUT.code ->
                CONNECTION_TIMEOUT.getApiErrorModel(context)
            NETWORK_NOT_CONNECT.code ->
                NETWORK_NOT_CONNECT.getApiErrorModel(context)
            NOT_LOGIN.code ->
                NOT_LOGIN.getApiErrorModel(context)
            SERVICE_FORBIDDEN.code ->
                SERVICE_FORBIDDEN.getApiErrorModel(context)
            else -> UNEXPECTED_ERROR.getApiErrorModel(context)
        }
        return "${apiErrorModel.status}  ${apiErrorModel.message}"
    }
    val apiErrorType: ApiErrorType = when (this) {
        is UnknownHostException -> NETWORK_NOT_CONNECT
        is ConnectException -> SERVICE_FORBIDDEN
        is SocketTimeoutException -> CONNECTION_TIMEOUT
        is JsonSyntaxException -> JSON_ERROR
        is MalformedJsonException -> JSON_ERROR
        is EOFException -> EOF_ERROR
        else -> this.formatSystemThrowable()
    }
    return "${apiErrorType.code}  ${apiErrorType.getApiErrorModel(context).message}"
}