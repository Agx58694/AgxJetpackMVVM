package com.agx.agxjetpackmvvmtest.ui.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbActivity
import com.agx.agxjetpackmvvmtest.databinding.ActivityMainBinding
import com.agx.agxjetpackmvvmtest.ui.fragment.main.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseDbActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun createObserver() {
        mViewModel.onErrorMsg.observe(this) {
            showErrorMessage(it)
        }
        mViewModel.dataResult.observe(this) { result ->
            result.onSuccess { showSuccessMessage(it) }
            result.onFailure { showErrorMessage(it.message.toString()) }
        }
    }

    override fun onNetworkStateChanged(netState: Boolean) {
        super.onNetworkStateChanged(netState)
        //网络状态变更，这里可以根据网络状态展示ui
        if (netState) {
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