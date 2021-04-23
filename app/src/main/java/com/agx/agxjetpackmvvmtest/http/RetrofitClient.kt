package com.agx.agxjetpackmvvmtest.http

import com.agx.agxjetpackmvvmtest.app.App
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.agx.jetpackmvvm.network.NetworkUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File

object RetrofitClient : BaseRetrofitClient() {

    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(), SharedPrefsCookiePersistor(
                App.CONTEXT
            )
        )
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {

        val httpCacheDirectory = File(App.CONTEXT.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        builder.cache(cache)
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                var request = chain.request()
                if (!NetworkUtil.isNetworkAvailable(App.CONTEXT)) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }
                val response = chain.proceed(request)
                if (!NetworkUtil.isNetworkAvailable(App.CONTEXT)) {
                    val maxAge = 60 * 60
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
                response
            }
    }
}