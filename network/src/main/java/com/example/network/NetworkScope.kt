package com.example.network

import androidx.lifecycle.MutableLiveData

class NetworkScope {
    suspend fun <T> launch(
        liveData: MutableLiveData<NetworkResources<T>>,
        call: suspend () -> T
    ) {
        liveData.value = NetworkResources.Loading
        kotlin.runCatching {
            call.invoke()
        }.onSuccess {
            liveData.value = NetworkResources.success(it)
        }.onFailure {
            liveData.value = NetworkResources.failure(it)
        }
    }
}


