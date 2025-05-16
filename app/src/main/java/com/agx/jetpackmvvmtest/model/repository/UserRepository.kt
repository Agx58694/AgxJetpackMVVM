package com.agx.jetpackmvvmtest.model.repository

import com.agx.jetpackmvvmtest.app.base.BaseRepository
import com.agx.jetpackmvvmtest.db.UserDatabase
import com.agx.jetpackmvvmtest.model.entity.UserEntity

class UserRepository(private val userDatabase: UserDatabase) : BaseRepository() {
    /**以下方法用于测试room*/
    suspend fun getAllUser(success: (List<UserEntity>?) -> Unit) {
        val allUserEntity = userDatabase.userDao().getAll()
        success.invoke(allUserEntity)
    }

    suspend fun insert(userEntity: UserEntity, success: () -> Unit) {
        userDatabase.userDao().insertAll(userEntity)
        success.invoke()
    }
}