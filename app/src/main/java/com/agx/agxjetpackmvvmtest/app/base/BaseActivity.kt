package com.agx.agxjetpackmvvmtest.app.base

import android.content.res.Resources
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.agx.agxjetpackmvvmtest.R
import com.agx.jetpackmvvm.base.activity.BaseVmDbActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    private var popupView: BasePopupView? = null

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        popupView = XPopup.Builder(this)
            .asLoading(message, R.layout.dialog_loading)
            .show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        popupView?.dismiss()
    }

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
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

}