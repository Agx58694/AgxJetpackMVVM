package com.agx.jetpackmvvmtest.ui.fragment.shared

import android.os.Bundle
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.agx.jetpackmvvm.ext.nav
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbFragment
import com.agx.jetpackmvvmtest.databinding.FragmentSharedElementTransitionABinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SharedElementTransitionAFragment :
    BaseDbFragment<SharedElementTransitionVM, FragmentSharedElementTransitionABinding>() {
    override fun layoutId() = R.layout.fragment_shared_element_transition_a

    override fun initVM(): SharedElementTransitionVM = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.imageCover.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                mDataBinding.imageCover to mDataBinding.imageCover.transitionName,
                mDataBinding.textContent to mDataBinding.textContent.transitionName
            )
            nav().navigate(
                R.id.action_sharedElementTransitionAFragment_to_sharedElementTransitionBFragment,
                null,
                null,
                extras
            )
        }
        mDataBinding.imageView.setOnClickListener {
            nav().popBackStack()
        }
    }

    override fun lazyLoadData() {
    }
}