package com.agx.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initVM(): VM

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
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
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observe(this, {
            showLoading()
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, {
            dismissLoading()
        })
    }
}