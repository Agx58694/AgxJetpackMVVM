package com.agx.jetpackmvvm

/**
 * 自定义异常类
 * ViewModel如使用原规则错误处理时，检测到该异常时可直接丢给ViewModel层*/
open class CustomException : RuntimeException {
    constructor()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)
}