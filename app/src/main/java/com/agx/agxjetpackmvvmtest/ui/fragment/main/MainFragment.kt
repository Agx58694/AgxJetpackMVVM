package com.agx.agxjetpackmvvmtest.ui.fragment.main

import android.os.Bundle
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentMainBinding
import com.agx.jetpackmvvm.ext.nav
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainFragment : BaseDbFragment<MainViewModel, FragmentMainBinding>() {
    override fun layoutId() = R.layout.fragment_main

    override fun initVM(): MainViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.button.setOnClickListener {
            nav()?.navigate(R.id.action_mainFragment_to_loginFragment)
        }
        mDataBinding.button2.setOnClickListener {
            nav()?.navigate(R.id.action_mainFragment_to_homeFragment)
        }
        mDataBinding.button3.setOnClickListener {
            nav()?.navigate(R.id.action_mainFragment_to_dbFragment)
        }
        mDataBinding.button12.setOnClickListener {
            nav()?.navigate(R.id.action_mainFragment_to_stateFragment)
        }
        mDataBinding.button15.setOnClickListener {
            nav()?.navigate(R.id.action_mainFragment_to_sharedElementTransitionAFragment)
        }
    }

    fun testData(){
        mViewModel.getData()
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {}

    override fun createViewObserver() {
        mViewModel.onErrorMsg.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

}