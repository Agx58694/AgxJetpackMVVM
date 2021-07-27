package com.agx.agxjetpackmvvmtest.http

import com.agx.agxjetpackmvvmtest.BuildConfig
import com.agx.agxjetpackmvvmtest.constant.TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }

            builder.addInterceptor(logging)
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)

            return builder.build()
        }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(StringConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }

    private class StringConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            inType: Type?,
            inAnnotations: Array<Annotation>?,
            inRetrofit: Retrofit?
        ): Converter<ResponseBody, String>? {
            return if (String::class.java == inType) {
                Converter { inValue -> inValue.string().replace("\"", "") }
            } else null
        }
    }
}
