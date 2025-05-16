package com.agx.jetpackmvvmtest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.agx.jetpackmvvmtest.model.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    suspend fun getAll(): List<UserEntity>

    @Upsert
    suspend fun insertAll(vararg userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)
}