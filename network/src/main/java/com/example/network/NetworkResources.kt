package com.example.network

sealed class NetworkResources<out T> {
    object Loading: NetworkResources<Nothing>()
    class Succeeded<out T>(val data: T): NetworkResources<T>()
    class Failure<out T>(val error: Any?): NetworkResources<T>()

    companion object {
        inline fun <T> success(data: T): NetworkResources<T> = Succeeded(data)
        inline fun <T> failure(exception: Throwable): Failure<T> = Failure(exception)
    }
}