package com.agx.agxjetpackmvvmtest.ui.fragment.flow

import android.os.Bundle
import android.util.Log
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentFlowBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FlowFragment : BaseDbFragment<FlowViewModel,FragmentFlowBinding>() {
    override fun layoutId() = R.layout.fragment_flow

    override fun initVM(): FlowViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun lazyLoadData() {}

    override fun createObserver() {
        mViewModel.onErrorMsg.observe(this){
            Log.d("Agx","如果这里不操作ui就写到这个函数里面")
        }
    }
}