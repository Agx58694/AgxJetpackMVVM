package com.agx.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mViewBinding: VB

    abstract fun layoutId(): Int

    abstract fun initVM(): VM

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    abstract fun showLoading(message: String)

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = DataBindingUtil.setContentView(this, layoutId())
        mViewModel = initVM()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        lifecycle.addObserver(NetworkStateManager.instance)
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        mViewModel.loadingChange.showDialog.observe(this, {
            showLoading(it)
        })
        mViewModel.loadingChange.dismissDialog.observe(this, {
            dismissLoading()
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}
}