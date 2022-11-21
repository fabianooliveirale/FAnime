package com.example.home.api

import com.example.home.model.NewVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface HomeService {
    @Headers("Content-Type: application/json")
    @GET("play-api.php?latest")
    suspend fun getNewVideos() : Response<List<NewVideosResponse>>
}