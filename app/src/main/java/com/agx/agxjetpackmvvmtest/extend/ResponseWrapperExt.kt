package com.agx.agxjetpackmvvmtest.extend

import com.agx.agxjetpackmvvmtest.constant.HTTP_OK
import com.agx.jetpackmvvm.ext.ifFalse
import com.agx.jetpackmvvm.ext.ifTrue
import com.agx.agxjetpackmvvmtest.http.ResponseWrapper


fun <T> ResponseWrapper<T>.unpacking(success: (T?) -> Unit, failure: (String) -> Unit = {}) {
    (code == HTTP_OK).ifTrue {
        success.invoke(data)
    }.ifFalse {
        failure.invoke(message)
    }
}