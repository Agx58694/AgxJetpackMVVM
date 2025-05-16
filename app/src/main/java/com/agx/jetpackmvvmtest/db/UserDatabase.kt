package com.agx.jetpackmvvmtest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agx.jetpackmvvmtest.model.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}