package com.agx.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mDataBinding: DB

    abstract fun layoutId(): Int

    abstract fun initVM(): VM

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewDataBinding()
        mViewModel = initVM()
        registorDefUIChange()
        initView(savedInstanceState)
        createObserver()
        lifecycle.addObserver(NetworkStateManager.instance)
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    abstract fun showLoading()

    abstract fun dismissLoading()

    /**
     * 创建DataBinding
     */
    private fun createViewDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, layoutId())
        mDataBinding.lifecycleOwner = this
    }

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.loadingChange.showDialog.observe(this, {
            showLoading()
        })
        mViewModel.loadingChange.dismissDialog.observe(this, {
            dismissLoading()
        })
    }
}