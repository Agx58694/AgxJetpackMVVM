package com.agx.jetpackmvvmtest.http

import com.agx.jetpackmvvmtest.app.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

object RetrofitClient : BaseRetrofitClient() {

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val httpCacheDirectory = File(App.CONTEXT.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
    }
}