package com.example.home.watching_videos

import androidx.lifecycle.ViewModel
import com.example.dao.SharedPref
import com.example.router.Router

class WatchingViewModel(
    private val sharedPref: SharedPref,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel() {


    fun getSharedPref() = sharedPref
    fun getImageUrl() = baseImageUrl
    fun getRouter() = router
}