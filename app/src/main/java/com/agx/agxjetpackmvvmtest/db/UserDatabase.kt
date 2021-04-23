package com.agx.agxjetpackmvvmtest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agx.agxjetpackmvvmtest.model.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}