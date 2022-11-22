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

    fun goToAnimesCategory(view: View, categoryName: String? = null) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${baseUri}/animesCategory?categoryName=${categoryName}".toUri())
            .build()
        view.findNavController().navigate(request)
    }

    fun goToAnimeDetails(view: View, animeId: String? = null) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${baseUri}/animeDetails?animeId=${animeId}".toUri())
            .build()
        view.findNavController().navigate(request)
    }
}