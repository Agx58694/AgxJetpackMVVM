package com.agx.agxjetpackmvvm.ui.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.agx.agxjetpackmvvm.R
import com.agx.agxjetpackmvvm.app.base.BaseActivity
import com.agx.agxjetpackmvvm.databinding.ActivityMainBinding
import com.agx.agxjetpackmvvm.ui.fragment.main.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.network.manager.NetState
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId() = R.layout.activity_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            ToastUtils.showShort("有网络")
        } else {
            ToastUtils.showShort("没有网络")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_navation).navigateUp()
    }
}