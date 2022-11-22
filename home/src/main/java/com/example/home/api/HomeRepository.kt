package com.example.home.api

import com.example.home.model.*

interface HomeRepository {
     suspend fun getNewVideos() : List<NewVideosResponse>
     suspend fun getAnimesCategory(categoryName: String) : List<AnimesCategoryResponse>
     suspend fun getAnimeEp(animeId: String) : List<AnimeEpResponse>
     suspend fun getAnimeDetails(animeId: String) : List<AnimeDetailsResponse>
     suspend fun getSearchAnime(searchValue: String) : List<SearchResponse>
}