package com.agx.jetpackmvvmtest.extend

import com.agx.jetpackmvvm.CustomException
import com.agx.jetpackmvvmtest.constant.HTTP_OK
import com.agx.jetpackmvvmtest.http.ResponseWrapper


fun <T> ResponseWrapper<T>.unpacking(): T? {
    if (code == HTTP_OK) {
        return data
    } else {
        throw CustomException(msg)
    }
}