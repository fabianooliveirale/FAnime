package com.example.home.api

import com.example.home.model.NewVideosResponse

interface HomeRepository {
     suspend fun getNewVideos() : List<NewVideosResponse>
}