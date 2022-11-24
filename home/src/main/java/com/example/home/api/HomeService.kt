package com.example.home.api

import com.example.model.*
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

    @Headers("Content-Type: application/json")
    @GET("play-api.php")
    suspend fun getAnimeEp(@Query("cat_id") animeId: String) : Response<List<AnimeEpResponse>>

    @Headers("Content-Type: application/json")
    @GET("play-api.php")
    suspend fun getAnimeDetails(@Query("info") animeId: String) : Response<List<AnimeDetailsResponse>>

    @Headers("Content-Type: application/json")
    @GET("play-api.php")
    suspend fun getSearchAnime(@Query("search") searchValue: String) : Response<List<SearchResponse>>
}