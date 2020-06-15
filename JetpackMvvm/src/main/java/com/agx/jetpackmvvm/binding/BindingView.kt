package com.agx.jetpackmvvm.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["visibility"])
fun View.visibility(visibilityState: Boolean){
    if(visibilityState){
        this.visibility = View.VISIBLE
        return
    }
    this.visibility = View.GONE
}

@BindingAdapter(value = ["isShowTextPwd"])
fun EditText.isShowTextPwd(boolean: Boolean){
    inputType = when(boolean) {
        true -> EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        false -> EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
    }
    setSelection(text.length)
}

@BindingAdapter(value = ["afterTextChanged"])
fun EditText.afterTextChanged(action: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            action()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

@BindingAdapter(value = ["setBackground"])
fun View.setBackground(@DrawableRes id: Int){
    background = ContextCompat.getDrawable(context,id)
}