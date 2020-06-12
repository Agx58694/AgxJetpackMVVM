package com.agx.agxjetpackmvvm.ui.fragment.home

import android.os.Bundle
import com.agx.agxjetpackmvvm.R
import com.agx.agxjetpackmvvm.app.base.BaseFragment
import com.agx.agxjetpackmvvm.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun layoutId() = R.layout.fragment_home

    override fun initVM(): HomeViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

}