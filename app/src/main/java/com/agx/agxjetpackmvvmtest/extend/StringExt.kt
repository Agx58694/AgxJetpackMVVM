package com.agx.agxjetpackmvvmtest.extend

import java.util.regex.Pattern

fun String?.isPhone(): Boolean {
    return this?.let {
        return Pattern.matches(
            "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$",
            it
        )
    } ?: let {
        false
    }
}