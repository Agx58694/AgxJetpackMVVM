package com.agx.jetpackmvvm.network

import android.content.Context
import androidx.annotation.StringRes
import com.agx.jetpackmvvm.R

enum class ApiErrorType(val code: Int, @param: StringRes private val messageId: Int) {
    INTERNAL_SERVER_ERROR(500, R.string.service_error),
    BAD_GATEWAY(502, R.string.service_error),
    NOT_FOUND(404, R.string.not_found),
    CONNECTION_TIMEOUT(408, R.string.timeout),
    NETWORK_NOT_CONNECT(499, R.string.network_wrong),
    UNEXPECTED_ERROR(700, R.string.unexpected_error),
    NOT_LOGIN(401, R.string.not_login),
    SERVICE_FORBIDDEN(403, R.string.service_forbidden),
    JSON_ERROR(1001, R.string.json_error),
    EOF_ERROR(1002, R.string.eof_error),
    GATEWAY_TIMEOUT(504, R.string.gateway_timeout),
    SYSTEM_ERROR(-1001, R.string.system_error),
    TIME_OUT(-1002,R.string.time_out);

    fun getApiErrorModel(context: Context): ApiErrorModel {
        return ApiErrorModel(code, context.getString(messageId))
    }
}
