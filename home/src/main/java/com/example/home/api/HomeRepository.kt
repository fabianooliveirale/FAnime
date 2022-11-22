package com.example.home.api

import com.example.home.model.AnimeDetailsResponse
import com.example.home.model.AnimesCategoryResponse
import com.example.home.model.AnimeEpResponse
import com.example.home.model.NewVideosResponse

interface HomeRepository {
     suspend fun getNewVideos() : List<NewVideosResponse>
     suspend fun getAnimesCategory(categoryName: String) : List<AnimesCategoryResponse>
     suspend fun getAnimeEp(animeId: String) : List<AnimeEpResponse>
     suspend fun getAnimeDetails(animeId: String) : List<AnimeDetailsResponse>
}