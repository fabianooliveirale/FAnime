package com.example.video.api

import com.example.video.model.VideoModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface VideoService {
    @Headers("Content-Type: application/json")
    @GET("play-api.php")
    suspend fun getLastReleases(@Query("episodios") episodesId: String) : Response<List<VideoModelResponse>>
}