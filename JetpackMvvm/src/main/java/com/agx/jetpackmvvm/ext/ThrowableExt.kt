package com.agx.jetpackmvvm.ext

import android.content.Context
import com.agx.jetpackmvvm.CustomException
import com.agx.jetpackmvvm.network.ApiErrorModel
import com.agx.jetpackmvvm.network.ApiErrorType
import com.agx.jetpackmvvm.network.ApiErrorType.BAD_GATEWAY
import com.agx.jetpackmvvm.network.ApiErrorType.CONNECTION_TIMEOUT
import com.agx.jetpackmvvm.network.ApiErrorType.EOF_ERROR
import com.agx.jetpackmvvm.network.ApiErrorType.GATEWAY_TIMEOUT
import com.agx.jetpackmvvm.network.ApiErrorType.INTERNAL_SERVER_ERROR
import com.agx.jetpackmvvm.network.ApiErrorType.JSON_ERROR
import com.agx.jetpackmvvm.network.ApiErrorType.NETWORK_NOT_CONNECT
import com.agx.jetpackmvvm.network.ApiErrorType.NOT_FOUND
import com.agx.jetpackmvvm.network.ApiErrorType.NOT_LOGIN
import com.agx.jetpackmvvm.network.ApiErrorType.SERVICE_FORBIDDEN
import com.agx.jetpackmvvm.network.ApiErrorType.SYSTEM_ERROR
import com.agx.jetpackmvvm.network.ApiErrorType.TIME_OUT
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * 系统原始错误*/
private var onAppThrowableListener: (Throwable) -> Unit = {}

/**
 * 其他错误处理*/
private var onOtherThrowableListener: (Throwable) -> String? = { null }

/**
 * 自定义错误格式化方法*/
private var onFormatThrowable: (Throwable, Context) -> String? = { throwable, context ->
    formatThrowableDefault(throwable, context)
}

fun setOnAppThrowableListener(it: (Throwable) -> Unit) {
    onAppThrowableListener = it
}

fun setOnFormatThrowable(it: (Throwable, Context) -> String) {
    onFormatThrowable = it
}

fun setOnOtherThrowableListener(it: (Throwable) -> String?) {
    onOtherThrowableListener = it
}

/**
 * 系统错误格式化扩展函数，该方法只返回原始错误，
 * @link setOnAppThrowableListener 建议拿到原始错误后做成日志保存以便异常排查*/
private fun Throwable.formatSystemThrowable(): ApiErrorType {
    onAppThrowableListener.invoke(this)
    //未预见的系统错误，只能统一格式化，该函数主要目的为上传原始错误到服务器记录
    return SYSTEM_ERROR
}

/**
 * 主动发送系统错误*/
fun Throwable.sendSystemThrowable() {
    onAppThrowableListener.invoke(this)
}

/**
 * 错误格式化扩展函数*/
fun Throwable.formatThrowable(context: Context): String? {
    return onFormatThrowable(this, context)
}

/**
 * 如不自定义错误格式化方法，默认使用该方法处理*/
private fun formatThrowableDefault(it: Throwable, context: Context): String? {
    if (it is CustomException) {
        return it.message
    }
    if (it is HttpException) {
        val apiErrorModel: ApiErrorModel = when (it.code()) {
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

            GATEWAY_TIMEOUT.code ->
                GATEWAY_TIMEOUT.getApiErrorModel(context)

            else -> {
                return "${it.code()}  ${it.message}"
            }
        }
        return "${apiErrorModel.status}  ${apiErrorModel.message}"
    }
    val apiErrorType: ApiErrorType = when (it) {
        is UnknownHostException -> NETWORK_NOT_CONNECT
        is ConnectException -> SERVICE_FORBIDDEN
        is SocketTimeoutException -> CONNECTION_TIMEOUT
        is JsonSyntaxException -> JSON_ERROR
        is MalformedJsonException -> JSON_ERROR
        is EOFException -> EOF_ERROR
        is TimeoutException -> TIME_OUT
        is TimeoutCancellationException -> TIME_OUT
        else -> {
            val errorMsg = onOtherThrowableListener.invoke(it)
            if (errorMsg == null) {
                it.formatSystemThrowable()
            } else {
                return errorMsg
            }
        }
    }
    return "${apiErrorType.code}  ${apiErrorType.getApiErrorModel(context).message}"
}