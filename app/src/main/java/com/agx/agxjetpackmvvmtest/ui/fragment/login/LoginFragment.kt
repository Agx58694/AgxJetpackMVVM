package com.agx.agxjetpackmvvmtest.ui.fragment.login

import android.os.Bundle
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseDbFragment
import com.agx.agxjetpackmvvmtest.databinding.LoginFragmentBinding
import com.agx.jetpackmvvm.ext.nav
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 登陆测试
 * 使用DataBinding*/
class LoginFragment : BaseDbFragment<LoginViewModel, LoginFragmentBinding>() {

    override fun layoutId() = R.layout.login_fragment

    override fun initVM(): LoginViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.viewModel = mViewModel
        mDataBinding.clickProxy = ClickProxy()
    }

    override fun lazyLoadData() {
        mViewModel.switchLoginMode()
    }

    //由于vm生命周期当前页面不一样，而且部分情况下比当前页面还要长，所以vm返回结果必须通过带生命周期的liveData发送过来
    override fun createObserver() {
        super.createObserver()
        mViewModel.onErrorMsg.observe(this) {
            showErrorMessage(it)
        }
        mViewModel.isShowTextPwd.observe(this) {
            editPassword.setSelection(editPassword.text.length)
        }
        mViewModel.loginResult.observe(this) { it ->
            it.onSuccess {
                showSuccessMessage(it)
            }
        }
    }

    inner class ClickProxy {
        fun buttonLoginClick() {
            mViewModel.login()
        }

        fun testClick(){
            nav().navigate(R.id.action_loginFragment_to_dbFragment)
        }
    }
}