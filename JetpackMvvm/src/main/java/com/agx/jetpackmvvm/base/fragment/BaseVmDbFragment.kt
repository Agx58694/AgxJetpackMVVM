package com.agx.jetpackmvvm.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvm.network.manager.NetState
import com.agx.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

    //是否第一次加载
    private var isFirst: Boolean = true
    //该类负责绑定视图数据的Viewmodel
    lateinit var mViewModel: VM
    //该类绑定的ViewDataBinding
    lateinit var mDatabind: DB

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
        mDatabind = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDatabind.lifecycleOwner = this
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = initVM()
        initView(savedInstanceState)
        onVisible()
        registorDefUIChange()
        initData()
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
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
            createObserver()
            NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
                onNetworkStateChanged(it)
            })
        }
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}

    abstract fun showLoading()

    abstract fun dismissLoading()


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        Log.d("AgxVm",mViewModel.loadingChange.toString())
        mViewModel.loadingChange.showDialog.observe(viewLifecycleOwner, Observer {
            showLoading()
        })
        mViewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoading()
        })
    }
}