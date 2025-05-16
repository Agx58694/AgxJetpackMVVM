package com.agx.jetpackmvvmtest.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbFragment
import com.agx.jetpackmvvmtest.databinding.FragmentHomeBinding
import com.agx.jetpackmvvmtest.ui.activity.DataStoreViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseDbFragment<HomeViewModel, FragmentHomeBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by activityViewModel()

    override fun layoutId() = R.layout.fragment_home

    override fun initVM(): HomeViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.click = ClickProxy()
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {}

    override fun createViewObserver() {
        mViewModel.onErrorMsg.collectIn(viewLifecycleOwner) {
            it ?: return@collectIn
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