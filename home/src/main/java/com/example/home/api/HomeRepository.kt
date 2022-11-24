package com.example.home.api

interface HomeRepository {
     suspend fun getNewVideos() : List<com.example.model.NewVideosResponse>
     suspend fun getAnimesCategory(categoryName: String) : List<com.example.model.AnimesCategoryResponse>
     suspend fun getAnimeEp(animeId: String) : List<com.example.model.AnimeEpResponse>
     suspend fun getAnimeDetails(animeId: String) : List<com.example.model.AnimeDetailsResponse>
     suspend fun getSearchAnime(searchValue: String) : List<com.example.model.SearchResponse>
}