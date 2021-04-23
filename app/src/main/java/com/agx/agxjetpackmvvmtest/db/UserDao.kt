package com.agx.agxjetpackmvvmtest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.agx.agxjetpackmvvmtest.model.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    suspend fun getAll(): List<UserEntity>?

    @Insert
    suspend fun insertAll(vararg userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)
}