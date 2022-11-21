package com.example.network

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NetworkBuilder {
    suspend fun <T> doRequest(call: suspend () -> Response<T>): T {
         val dispatcher: CoroutineDispatcher = Dispatchers.IO
         val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(
                "NetworkBuilderError",
                throwable.message,
                throwable
            )
        }

        return withContext(dispatcher + exceptionHandler) {
            val response = call.invoke()

            response.body() ?: throw IllegalStateException("body reponse is null")
        }
    }
}