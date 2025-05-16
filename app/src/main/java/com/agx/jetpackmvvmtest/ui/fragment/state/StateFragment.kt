package com.agx.jetpackmvvmtest.ui.fragment.state

import android.os.Bundle
import android.view.View
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbFragment
import com.agx.jetpackmvvmtest.databinding.FragmentStateBinding
import com.agx.jetpackmvvmtest.ui.custom.EmptyCallback
import com.agx.jetpackmvvmtest.ui.custom.ErrorCallback
import com.agx.jetpackmvvmtest.ui.custom.LoadingCallback
import com.agx.jetpackmvvmtest.ui.custom.NoNetworkCallback
import com.agx.jetpackmvvmtest.ui.custom.TimeoutCallback
import org.koin.androidx.viewmodel.ext.android.getViewModel

class StateFragment : BaseDbFragment<StateViewModel, FragmentStateBinding>() {


    override fun layoutId() = R.layout.fragment_state

    override fun initVM(): StateViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.click = ClickProxy()
    }

    override fun lazyLoadData() {
        mViewModel.loadData(1)
    }

    override fun createObserver() {}

    override fun createViewObserver() {
        mViewModel.onErrorMsg.collectIn(viewLifecycleOwner) {
            it ?: return@collectIn
            showErrorMessage(it)
        }
        mViewModel.loadDataResult.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                mLoadService.showCallback(EmptyCallback::class.java)
            } else {
                mLoadService.showSuccess()
            }
        }
    }

    override fun layoutDataLoading() {
        mLoadService.showCallback(LoadingCallback::class.java)
    }

    override fun layoutDataError(errorMessage: String?) {
        mLoadService.showCallback(ErrorCallback::class.java)
    }

    override fun layoutDataTimeout() {
        mLoadService.showCallback(TimeoutCallback::class.java)
    }

    override fun onReload(v: View) {
        mViewModel.loadData(1)
    }

    override fun onNetworkStateChanged(netState: Boolean) {
        if (netState) {
            mViewModel.loadData(1)
        } else {
            mLoadService.showCallback(NoNetworkCallback::class.java)
        }
    }

    inner class ClickProxy {
        fun loadSuccess() {
            mViewModel.loadData(1)
        }

        fun loadError() {
            mViewModel.loadData(2)
        }

        fun loadEmpty() {
            mViewModel.loadData(3)
        }

        fun loadTimeout() {
            mViewModel.loadData(4)
        }
    }
}