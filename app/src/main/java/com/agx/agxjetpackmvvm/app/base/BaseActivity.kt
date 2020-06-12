package com.agx.agxjetpackmvvm.app.base

import android.content.res.Resources
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.agx.agxjetpackmvvm.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.jessyan.autosize.AutoSizeCompat

/**
 * 时间　: 2019/12/21
 * 作者　: hegaojian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
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
    override fun showLoading() {
        popupView = XPopup.Builder(this).asLoading()
            .bindLayout(R.layout.dialog_loading)
            .show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        popupView?.dismiss()
    }

    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

}