package com.agx.jetpackmvvmtest.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class AgxWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        while (true) {
            delay(1000)
            Log.d("Agx", "AgxWorker正在运行")
        }
        return Result.success()
    }
}