package com.agx.jetpackmvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.ext.ifTrue
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    //是否第一次加载
    private var isFirst: Boolean = true

    //该类负责绑定视图数据的ViewModel
    lateinit var mViewModel: VM

    //该类绑定的ViewDataBinding
    lateinit var mDataBinding: DB

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    /**
     * ViewModel*/
    abstract fun initVM(): VM

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 界面数据加载中*/
    abstract fun layoutDataLoading()

    /**
     * 界面数据加载失败*/
    abstract fun layoutDataError(errorMessage: String)

    /**
     * 请求超时*/
    abstract fun layoutDataTimeout()

    abstract fun showLoading(message: String)

    abstract fun dismissLoading()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = initVM()
        registerDefUIChange()
        initView(savedInstanceState)
        onVisible()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED) {
            (isFirst).ifTrue {
                lazyLoadData()
                isFirst = false
                createObserver()
            }
            NetworkStateManager.instance.mNetworkStateCallback.observe(viewLifecycleOwner, {
                onNetworkStateChanged(it)
            })
        }
    }

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.loadingChange.showDialog.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        mViewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner) {
            dismissLoading()
        }
        mViewModel.layoutDataChange.layoutDataLoading.observe(viewLifecycleOwner) {
            layoutDataLoading()
        }
        mViewModel.layoutDataChange.layoutDataError.observe(viewLifecycleOwner) {
            layoutDataError(it)
        }
        mViewModel.layoutDataChange.layoutDataTimeout.observe(viewLifecycleOwner){
            layoutDataTimeout()
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 初始化监听器*/
    open fun initListener() {}
}