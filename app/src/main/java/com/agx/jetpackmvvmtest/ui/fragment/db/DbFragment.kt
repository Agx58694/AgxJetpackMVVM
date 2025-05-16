package com.agx.jetpackmvvmtest.ui.fragment.db

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.agx.jetpackmvvm.ext.collectIn
import com.agx.jetpackmvvmtest.R
import com.agx.jetpackmvvmtest.app.base.BaseDbFragment
import com.agx.jetpackmvvmtest.databinding.FragmentDbBinding
import com.agx.jetpackmvvmtest.model.entity.UserEntity
import com.agx.jetpackmvvmtest.ui.activity.DataStoreViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 数据库测试*/
class DbFragment : BaseDbFragment<DbViewModel, FragmentDbBinding>() {
    private val dataStoreViewModel: DataStoreViewModel by activityViewModel()

    override fun layoutId() = R.layout.fragment_db

    override fun initVM(): DbViewModel = getViewModel()

    override fun initView(savedInstanceState: Bundle?) {}

    override fun lazyLoadData() {}

    override fun initListener() {
        mDataBinding.button4.setOnClickListener {
            mViewModel.insert(
                UserEntity(
                    id = 1,
                    realName = "周星星",
                    nikeName = "周洁轮",
                    age = 22
                )
            )
        }
        mDataBinding.button5.setOnClickListener {
            mViewModel.getAllUser()
        }
        mDataBinding.button6.setOnClickListener {
            mViewModel.test()
        }
        mDataBinding.button7.setOnClickListener {
            dataStoreViewModel.saveUserName("哈哈一笑")
        }
    }

    override fun createObserver() {}

    override fun createViewObserver() {
        mViewModel.onErrorMsg.collectIn(viewLifecycleOwner) {
            it ?: return@collectIn
            showErrorMessage(it)
            //如果有种情况是弹出错误，然后关闭该界面。因为使用了协程1.5秒关闭错误框,所以错误框未关闭时关闭该fm则会发生错误框无法完全显示问题。下面方式可解决
//            showErrorMessage(it){
//                //关闭错误框后关闭该界面
//                nav().popBackStack()
//            }
        }
        mViewModel.getAllUserResult.observe(viewLifecycleOwner) { it ->
            it.onSuccess {
                showSuccessMessage(it.toString())
            }
        }
        mViewModel.userInsertResult.observe(viewLifecycleOwner) { it ->
            it.onSuccess {
                showSuccessMessage("插入成功")
            }
            it.onFailure {
                showErrorMessage(it.message.toString())
            }
        }
        mViewModel.testResult.observe(viewLifecycleOwner) {
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
        dataStoreViewModel.userName.asLiveData().observe(viewLifecycleOwner) {
            showSuccessMessage(it.toString())
        }
    }
}