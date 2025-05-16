package com.agx.jetpackmvvmtest.extend

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.agx.jetpackmvvmtest.constant.DATA_STORE_PREFERENCES_NAME

val Context.dataStore by preferencesDataStore(name = DATA_STORE_PREFERENCES_NAME)