package com.example.router

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController

class Router(private val baseUri: String) {
    fun goToVideo(
        activity: Activity?,
        playerActivity: Activity?,
        videoId: String,
        animeId: String,
        title: String,
        imageUrl: String,
        position: Int = 0
    ) {
        val intent = Intent(activity, playerActivity?.javaClass)
        intent.putExtra("videoId", videoId)
        intent.putExtra("animeId", animeId)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("title", title)
        intent.putExtra("position", position)
        activity?.startActivity(intent)
    }

    fun goToAllWatching(view: View, where: String? = null) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${baseUri}/allWatching?where=${where}".toUri())
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