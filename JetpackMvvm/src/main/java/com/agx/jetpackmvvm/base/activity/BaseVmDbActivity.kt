package com.agx.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvm.startup.ConnectionInitializer

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mDataBinding: DB

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
        createViewDataBinding()
        mViewModel = initVM()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        ConnectionInitializer.connectionLiveData.observe(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * 创建DataBinding
     */
    private fun createViewDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, layoutId())
        mDataBinding.lifecycleOwner = this
    }

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        mViewModel.loadingChange.showDialog.collectIn(this) {
            showLoading(it)
        }
        mViewModel.loadingChange.dismissDialog.collectIn(this) {
            dismissLoading()
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: Boolean) {}
}