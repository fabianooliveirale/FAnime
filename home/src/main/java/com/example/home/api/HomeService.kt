package com.example.home.api

import com.example.home.model.AnimesCategoryResponse
import com.example.home.model.NewVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface HomeService {
    @Headers("Content-Type: application/json")
    @GET("play-api.php?latest")
    suspend fun getNewVideos() : Response<List<NewVideosResponse>>

    @Headers("Content-Type: application/json")
    @GET("play-api.php")
    suspend fun getAnimesCategory(@Query("categoria") categoryName: String) : Response<List<AnimesCategoryResponse>>
}