package com.agx.jetpackmvvm.ext

inline fun Boolean.ifTrue(defaultValue: () -> Unit): Boolean {
    if (this) {
        defaultValue()
    }
    return this
}

inline fun Boolean.ifFalse(defaultValue: () -> Unit): Boolean {
    if (!this) {
        defaultValue()
    }
    return this
}