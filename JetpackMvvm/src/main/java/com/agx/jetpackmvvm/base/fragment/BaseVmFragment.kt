package com.agx.jetpackmvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmFragment<VM : BaseViewModel> : Fragment() {

    //是否第一次加载
    private var isFirst: Boolean = true

    lateinit var mViewModel: VM

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    abstract fun initVM(): VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = initVM()
        initView(savedInstanceState)
        onVisible()
        registerDefUIChange()
        initData()
        initListener()
        NetworkStateManager.instance.mNetworkStateCallback.observe(viewLifecycleOwner, {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化监听器*/
    open fun initListener() {}

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED) {
            if (isFirst) {
                lazyLoadData()
                isFirst = false
                createObserver()
            } else {
                onVisibleResume()
            }
        }
    }

    /**
     * 重新展示（非第一次）*/
    open fun onVisibleResume() {}

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}


    abstract fun showLoading()

    abstract fun dismissLoading()


    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.loadingChange.showDialog.observe(viewLifecycleOwner, {
            showLoading()
        })
        mViewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner, {
            dismissLoading()
        })
    }
}