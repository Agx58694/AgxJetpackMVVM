package com.agx.agxjetpackmvvmtest.ui.activity

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.agx.agxjetpackmvvmtest.constant.USER_NAME
import com.agx.agxjetpackmvvmtest.extend.dataStore
import com.agx.jetpackmvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
/**
 * 该ViewModel专用于操作DataStore数据
 * 单Activity使用全局ViewModel*/
class DataStoreViewModel(application: Application) : BaseViewModel(application) {
    private val userNameKey = stringPreferencesKey(USER_NAME)
    val userName: Flow<String?> = getContext().dataStore.data.map { it[userNameKey] }

    fun saveUserName(name: String) = launch(
        block = {
            getContext().dataStore.edit {
                it[userNameKey] = name
            }
        }
    )
}