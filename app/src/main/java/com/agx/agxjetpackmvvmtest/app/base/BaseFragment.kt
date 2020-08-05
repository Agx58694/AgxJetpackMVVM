package com.agx.agxjetpackmvvmtest.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.agx.agxjetpackmvvmtest.R
import com.agx.jetpackmvvm.base.fragment.BaseVmDbFragment
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　: 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {
    private var popupView: BasePopupView? = null
    /**
     * 当前Fragment绑定的视图布局
     */
    abstract override fun layoutId(): Int


    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    abstract override fun lazyLoadData()

    /**
     * 创建LiveData观察者 懒加载之后才会触发
     */
    override fun createObserver() {}


    /**
     * Fragment执行onViewCreated后触发的方法
     */
    override fun initData() {

    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * 打开等待框
     */
    override fun showLoading() {
        popupView = XPopup.Builder(context)
            .asLoading("加载中",R.layout.dialog_loading)
            .show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        popupView?.dismiss()
    }
}