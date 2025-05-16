package com.agx.jetpackmvvmtest.ui.activity

import android.os.Bundle
import androidx.navigation.findNavController
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvm.ext.sendSystemThrowable
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbActivity
import com.agx.jetpackmvvmtest.databinding.ActivityMainBinding
import com.agx.jetpackmvvmtest.ui.fragment.main.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseDbActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun createObserver() {
        mViewModel.onErrorMsg.collectIn(this) {
            it ?: return@collectIn
            showErrorMessage(it)
        }
        mViewModel.dataResult.observe(this) { result ->
            result.onSuccess { showSuccessMessage(it) }
            result.onFailure { showErrorMessage(it.message.toString()) }
        }
        RuntimeException("发送错误").sendSystemThrowable()
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
        return this.findNavController(R.id.main_navation).navigateUp()
    }
}