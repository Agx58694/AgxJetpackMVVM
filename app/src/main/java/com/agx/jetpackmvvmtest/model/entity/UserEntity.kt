package com.agx.jetpackmvvmtest.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "real_name") val realName: String,
    @ColumnInfo(name = "nike_name") val nikeName: String,
    @ColumnInfo(name = "age") val age: Int?
)
