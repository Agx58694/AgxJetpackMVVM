package com.agx.agxjetpackmvvm.ui.fragment.login

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.agx.agxjetpackmvvm.R
import com.agx.agxjetpackmvvm.app.base.BaseFragment
import com.agx.agxjetpackmvvm.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginFragment : BaseFragment<LoginViewModel, LoginFragmentBinding>() {

    override fun layoutId() = R.layout.login_fragment

    override fun initVM(): LoginViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.clickProxy = ClickProxy()
    }

    override fun lazyLoadData() {
        mViewModel.switchLoginMode()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.isShowTextPwd.observe(viewLifecycleOwner, Observer {
            editPassword.setSelection(editPassword.text.length)
        })
        mViewModel.onErrorMsg.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    inner class ClickProxy{
        fun buttonLoginClick(){
            mViewModel.login()
        }
    }
}