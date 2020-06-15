package com.agx.agxjetpackmvvmtest.ui.fragment.main

import android.os.Bundle
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import com.agx.jetpackmvvm.ext.nav
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {
    override fun layoutId() = R.layout.fragment_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
       button.setOnClickListener {
           nav().navigate(R.id.action_mainFragment_to_loginFragment)
       }
    }

    override fun lazyLoadData() {
    }

}