package com.agx.agxjetpackmvvmtest.ui.custom

import com.agx.agxjetpackmvvmtest.R
import com.kingja.loadsir.callback.Callback

class EmptyCallback : Callback() {
    override fun onCreateView() = R.layout.layout_empty
}