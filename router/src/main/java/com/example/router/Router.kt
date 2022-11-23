package com.example.router

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController

class Router(private val baseUri: String) {
    fun goToVideo(intent: Fragment, view: View, videoId: String? = null) {
        val i = Intent(ACTION_VIEW,"${baseUri}/video?videoId=${videoId}".toUri())
        intent.startActivity(i)
       // view.findNavController().navigate(request)
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