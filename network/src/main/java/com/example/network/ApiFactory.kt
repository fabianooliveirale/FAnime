package com.example.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory(private val gson: Gson,private val okHttpClient: OkHttpClient) {
    fun <T> create(baseUrl: String, service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(service)
    }

    fun <T : Any> createApiImage(baseUrl: String, service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(service)
    }
}

