package com.agx.agxjetpackmvvmtest.extend

import com.agx.agxjetpackmvvmtest.constant.HTTP_OK
import com.agx.agxjetpackmvvmtest.http.ResponseWrapper
import com.agx.jetpackmvvm.CustomException


fun <T> ResponseWrapper<T>.unpacking(): T? {
    if (code == HTTP_OK) {
        return data
    } else {
        throw CustomException(msg)
    }
}