package com.agx.agxjetpackmvvmtest.ui.fragment.db

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.agx.agxjetpackmvvmtest.db.UserDatabase
import com.agx.agxjetpackmvvmtest.model.entity.UserEntity
import com.agx.agxjetpackmvvmtest.model.repository.UserRepository
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import java.lang.RuntimeException

class DbViewModel(application: Application,private val userRepository: UserRepository) : BaseViewModel(application) {
    val getAllUserResult = MutableLiveData<Result<List<UserEntity>?>>()
    val userInsertResult = MutableLiveData<Result<Any?>>()

    /**以下方法用于测试room*/
    fun getAllUser() = needLoadingLaunch(
        block = {
            userRepository.getAllUser {
                getAllUserResult.value = Result.success(it)
            }
        }
    )

    fun insert(userEntity: UserEntity) = needLoadingLaunch(
        block = {
            userRepository.insert(
                userEntity = userEntity,
                success = { userInsertResult.value = Result.success(null) }
            )
        },
        onError = {
            //如果是加载错误需要关闭刷新状态的话请在这里发射
            userInsertResult.value = Result.failure(RuntimeException(it))
        }
    )

    //如一个界面同一时间并发了两个以上带加载协程的话，请使用一下方法。否则会同时展出多个等待框，并且无法全部关闭
    fun test() = needLoadingLaunch(
        block = {
            test1()
            test2()
            test3()
        }
    )

    private suspend fun test1(){
        delay(1000)
    }
    private suspend fun test2(){
        delay(1000)
    }
    private suspend fun test3(){
        delay(1000)
    }
}