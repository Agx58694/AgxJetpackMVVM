package com.agx.agxjetpackmvvmtest.ui.fragment.shared

import android.os.Bundle
import androidx.transition.TransitionInflater
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentSharedElementTransitionABinding
import com.agx.agxjetpackmvvmtest.databinding.FragmentSharedElementTransitionBBinding
import com.agx.jetpackmvvm.ext.nav
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SharedElementTransitionBFragment : BaseDbFragment<SharedElementTransitionVM, FragmentSharedElementTransitionBBinding>() {
    override fun layoutId() = R.layout.fragment_shared_element_transition_b

    override fun initVM(): SharedElementTransitionVM = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_shared_element_transition)
        mDataBinding.imageCover.setOnClickListener {
            nav()?.navigate(R.id.action_sharedElementTransitionBFragment_to_sharedElementTransitionAFragment2)
        }
    }

    override fun lazyLoadData() {
    }
}