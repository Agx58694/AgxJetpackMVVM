package com.agx.jetpackmvvmtest.ui.fragment.db

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import com.agx.jetpackmvvmtest.model.entity.UserEntity
import com.agx.jetpackmvvmtest.model.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class DbViewModel(application: Application, private val userRepository: UserRepository) :
    BaseViewModel(application) {
    val getAllUserResult = MutableLiveData<Result<List<UserEntity>?>>()
    val userInsertResult = MutableLiveData<Result<Any?>>()
    val testResult = MutableLiveData<Int>()

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
            val a = async { test1() }
            val b = async { test2() }
            val c = async { test3() }
            val d = async { test4() }
            val e = async { test5() }
            testResult.value = a.await() + b.await() + c.await() + d.await() + e.await()
        }
    )

    private suspend fun test1(): Int {
        delay(1300)
        return 10
    }

    private suspend fun test2(): Int {
        delay(1200)
//        这个错误不会引发全局错误配置器，只会触发提示错误
//        throw CustomException("我是自定义错误")
        return 15
    }

    private suspend fun test3(): Int {
        delay(700)
//        这里会触发全局错误配置器，并触发提示格式化后的错误信息
//        throw RuntimeException("error")
        return 32
    }

    private suspend fun test4(): Int {
        delay(1000)
        return 6
    }

    private suspend fun test5(): Int {
        delay(100)
        return 23
    }
}