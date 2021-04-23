package com.agx.agxjetpackmvvmtest.ui.fragment.home

import android.os.Bundle
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseDbFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun layoutId() = R.layout.fragment_home

    override fun initVM(): HomeViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

}