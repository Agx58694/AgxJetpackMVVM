package com.agx.jetpackmvvmtest.ui.fragment.flow

import android.os.Bundle
import android.util.Log
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbFragment
import com.agx.jetpackmvvmtest.databinding.FragmentFlowBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FlowFragment : BaseDbFragment<FlowViewModel, FragmentFlowBinding>() {
    override fun layoutId() = R.layout.fragment_flow

    override fun initVM(): FlowViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun lazyLoadData() {}

    override fun createObserver() {
        mViewModel.onErrorMsg.collectIn(this) {
            Log.d("Agx", "如果这里不操作ui就写到这个函数里面")
        }
    }
}