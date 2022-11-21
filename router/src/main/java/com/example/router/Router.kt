package com.example.router

import android.view.View
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController

class Router(private val baseUri: String) {
    fun goToVideo(view: View, videoId: String? = null) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${baseUri}/video?videoId=${videoId}".toUri())
            .build()
        view.findNavController().navigate(request)
    }

    fun goToHome(view: View) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${baseUri}/home".toUri())
            .build()
        view.findNavController().navigate(request)
    }
}