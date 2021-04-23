package com.agx.agxjetpackmvvmtest.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.agx.jetpackmvvm.base.activity.BaseVmDbActivity
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    private var tipDialog: QMUITipDialog? = null

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 弹出错误框*/
    fun showErrorMessage(errorMessage: String) {
        QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setTipWord(errorMessage)
            .create().apply {
                lifecycleScope.launch {
                    delay(1500)
                    dismiss()
                }
            }.show()
    }

    /**
     * 弹出成功框*/
    fun showSuccessMessage(successMessage: String) {
        QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
            .setTipWord(successMessage)
            .create().apply {
                lifecycleScope.launch {
                    delay(1500)
                    dismiss()
                }
            }.show()

    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        tipDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(message)
            .create()
        tipDialog?.show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        tipDialog?.dismiss()
    }

}