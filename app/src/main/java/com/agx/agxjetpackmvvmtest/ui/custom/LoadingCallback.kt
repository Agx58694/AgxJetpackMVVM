package com.agx.agxjetpackmvvmtest.ui.custom

import android.content.Context
import android.view.View
import com.agx.agxjetpackmvvmtest.R
import com.kingja.loadsir.callback.Callback

class LoadingCallback : Callback() {
    override fun onCreateView() = R.layout.layout_loading

    override fun onReloadEvent(context: Context?, view: View?) = true
}