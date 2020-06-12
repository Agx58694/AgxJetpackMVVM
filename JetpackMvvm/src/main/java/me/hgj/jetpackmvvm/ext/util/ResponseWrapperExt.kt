package me.hgj.jetpackmvvm.ext.util

import me.hgj.jetpackmvvm.network.bean.ResponseWrapper

fun <T> ResponseWrapper<T>.unpacking(success: (T?) -> Unit,failure: (String) -> Unit = {}){
    (code == 200).ifTrue {
        success.invoke(data)
    }.ifFalse {
        failure.invoke(message)
    }
}