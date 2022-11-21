package com.example.fanime.di

import com.example.network.ApiFactory
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object NetworkModule {
    fun get() = module {
        single {
            Gson()
        }

        single {
            HttpLoggingInterceptor()
        }

        single {
            OkHttpClient().newBuilder().connectTimeout(6000, TimeUnit.MILLISECONDS)
                .readTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(get<HttpLoggingInterceptor>().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(createMozillaInterceptor())
                .cache(null)
                .build()
        }

        single {
            ApiFactory(get(), get())
        }
    }

    private fun createMozillaInterceptor() = Interceptor { chain ->
        return@Interceptor chain.proceed(
            chain.request().newBuilder().addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0"
            ).build()
        )
    }
}