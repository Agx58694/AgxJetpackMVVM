package com.agx.agxjetpackmvvmtest.extend

import com.agx.jetpackmvvm.ext.util.ifFalse
import com.agx.jetpackmvvm.ext.util.ifTrue
import com.agx.jetpackmvvm.network.bean.ResponseWrapper


fun <T> ResponseWrapper<T>.unpacking(success: (T?) -> Unit, failure: (String) -> Unit = {}){
    (code == 200).ifTrue {
        success.invoke(data)
    }.ifFalse {
        failure.invoke(message)
    }
}