package com.agx.agxjetpackmvvmtest.ui.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbActivity
import com.agx.agxjetpackmvvmtest.databinding.ActivityMainBinding
import com.agx.agxjetpackmvvmtest.ui.fragment.main.MainViewModel
import com.agx.jetpackmvvm.ext.setOnAppThrowableListener
import com.blankj.utilcode.util.ToastUtils
import com.agx.jetpackmvvm.network.manager.NetState
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseDbActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        //处理viewModel原始错误，所以viewModel的原始错误都会到这里，开发者可记录日子或上传服务器记录
        setOnAppThrowableListener {
            showErrorMessage(it.message.toString())
        }
    }

    override fun createObserver() {
        mViewModel.onErrorMsg.observe(this) {
            showErrorMessage(it)
        }
        mViewModel.dataResult.observe(this) { result ->
            result.onSuccess { showSuccessMessage(it) }
            result.onFailure { showErrorMessage(it.message.toString()) }
        }
    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        //网络状态变更，这里可以根据网络状态展示ui
        if (netState.isSuccess) {
            ToastUtils.showShort("有网络")
        } else {
            ToastUtils.showShort("没有网络")
        }
    }

    //设置nav导航启动菜单
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_navation).navigateUp()
    }
}