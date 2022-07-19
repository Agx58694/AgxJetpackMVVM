package com.agx.agxjetpackmvvmtest.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentHomeBinding
import com.agx.agxjetpackmvvmtest.ui.activity.DataStoreViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseDbFragment<HomeViewModel, FragmentHomeBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by sharedViewModel()

    override fun layoutId() = R.layout.fragment_home

    override fun initVM(): HomeViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.click = ClickProxy()
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {}

    override fun createViewObserver() {
        mViewModel.onErrorMsg.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
        dataStoreViewModel.userName.asLiveData().observe(viewLifecycleOwner) {
            showSuccessMessage(it.toString())
        }
    }

    inner class ClickProxy {
        fun buttonClick() {
            dataStoreViewModel.saveUserName("周星星")
        }
    }

}