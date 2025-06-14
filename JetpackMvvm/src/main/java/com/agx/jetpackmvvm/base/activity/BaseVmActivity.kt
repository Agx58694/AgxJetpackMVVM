package com.agx.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvm.startup.ConnectionInitializer

abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initVM(): VM

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    abstract fun showLoading(message: String)

    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        mViewModel = initVM()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        ConnectionInitializer.connectionLiveData.observe(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.collectIn(this) {
            showLoading(it)
        }

        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.collectIn(this) {
            dismissLoading()
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: Boolean) {}
}