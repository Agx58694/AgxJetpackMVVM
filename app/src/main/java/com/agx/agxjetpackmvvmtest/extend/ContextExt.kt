package com.agx.agxjetpackmvvmtest.extend

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.agx.agxjetpackmvvmtest.constant.DATA_STORE_PREFERENCES_NAME

val Context.dataStore by preferencesDataStore(name = DATA_STORE_PREFERENCES_NAME)