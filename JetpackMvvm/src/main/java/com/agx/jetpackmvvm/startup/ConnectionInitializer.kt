package com.agx.jetpackmvvm.startup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.startup.Initializer
import com.agx.jetpackmvvm.network.ConnectionLiveData

class ConnectionInitializer : Initializer<ConnectionLiveData> {
    companion object{
        lateinit var connectionLiveData: LiveData<Boolean>
    }

    override fun create(context: Context): ConnectionLiveData {
        connectionLiveData = ConnectionLiveData(context)
        return connectionLiveData as ConnectionLiveData
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}