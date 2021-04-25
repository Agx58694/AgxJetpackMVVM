package com.agx.agxjetpackmvvmtest.ui.fragment.db

import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.agx.agxjetpackmvvmtest.R
import com.agx.agxjetpackmvvmtest.app.base.BaseVbFragment
import com.agx.agxjetpackmvvmtest.databinding.FragmentDbBinding
import com.agx.agxjetpackmvvmtest.model.entity.UserEntity
import com.agx.agxjetpackmvvmtest.ui.activity.DataStoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.RuntimeException

/**
 * 数据库测试
 * 使用ViewBinding*/
class DbFragment : BaseVbFragment<DbViewModel,FragmentDbBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by sharedViewModel()

    override fun layoutId() = R.layout.fragment_db

    override fun initVM(): DbViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun lazyLoadData() {}

    override fun initListener() {
        mViewBinding.button4.setOnClickListener {
            mViewModel.insert(UserEntity(realName = "周星星",nikeName = "周洁轮",age = 22))
        }
        mViewBinding.button5.setOnClickListener {
            mViewModel.getAllUser()
        }
        mViewBinding.button6.setOnClickListener {
            mViewModel.test()
        }
        mViewBinding.button7.setOnClickListener {
            dataStoreViewModel.saveUserName("哈哈一笑")
        }
    }

    override fun createObserver() {
        mViewModel.onErrorMsg.observe(this){
            showErrorMessage(it)
            //如果有种情况是弹出错误，然后关闭该界面。因为使用了协程1.5秒关闭错误框。所以错误框未关闭时关闭该fm则会发生错误框无法关闭问题。下面方式可解决
//            showErrorMessage(it){
//                //关闭错误框后关闭该界面
//                nav().popBackStack()
//            }
        }
        mViewModel.getAllUserResult.observe(this){ it ->
            it.onSuccess {
                showSuccessMessage(it.toString())
            }
        }
        mViewModel.userInsertResult.observe(this){
            it.onSuccess {
                showSuccessMessage("插入成功")
            }
            it.onFailure {
                showErrorMessage(it.message.toString())
            }
        }
        mViewModel.testResult.observe(this){
            showSuccessMessage(it.toString())
//            这里会出现闪退
//            lifecycleScope.launch {
//                delay(1000)
//                throw RuntimeException("这里尝试抛出一个错误")
//            }
//            ------------------------分割线--------------------------
//            //这里不会触发闪退，但会触发全局错误配置器，并触发提示格式化后的错误信息
//            lifecycleScope.launch(mViewModel.coroutineContext){
//                delay(1000)
//                throw RuntimeException("这里尝试抛出一个错误")
//            }
        }
        dataStoreViewModel.userName.asLiveData().observe(this){
            showSuccessMessage(it.toString())
        }
    }
}