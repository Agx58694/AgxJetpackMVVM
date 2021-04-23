package com.agx.agxjetpackmvvmtest.model.repository

import com.agx.agxjetpackmvvmtest.app.base.BaseRepository
import com.agx.agxjetpackmvvmtest.db.UserDatabase
import com.agx.agxjetpackmvvmtest.model.entity.UserEntity

class UserRepository(private val userDatabase: UserDatabase) : BaseRepository() {
    /**以下方法用于测试room*/
    suspend fun getAllUser(success: (List<UserEntity>?) -> Unit) {
        val allUserEntity = userDatabase.userDao().getAll()
        success.invoke(allUserEntity)
    }

    suspend fun insert(userEntity: UserEntity,success: () -> Unit){
        userDatabase.userDao().insertAll(userEntity)
        success.invoke()
    }
}