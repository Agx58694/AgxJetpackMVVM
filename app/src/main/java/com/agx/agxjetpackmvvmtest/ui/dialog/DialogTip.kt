package com.agx.agxjetpackmvvmtest.ui.dialog

import android.content.Context
import com.agx.agxjetpackmvvmtest.R
import com.lxj.xpopup.core.BottomPopupView

class DialogTip(context: Context) : BottomPopupView(context) {
    override fun getImplLayoutId() = R.layout.dialog_tips

    override fun onCreate() {
        super.onCreate()
    }
}