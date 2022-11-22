package com.example.home.api

import com.example.home.model.AnimeDetailsResponse
import com.example.home.model.AnimeEpResponse
import com.example.home.model.AnimesCategoryResponse
import com.example.home.model.NewVideosResponse
import com.example.network.NetworkBuilder

class HomeRepositoryImpl(
    private val networkBuilder: NetworkBuilder,
    private val service: HomeService
) : HomeRepository {

    override suspend fun getNewVideos(): List<NewVideosResponse> {
        return networkBuilder.doRequest {
            service.getNewVideos()
        }
    }

    override suspend fun getAnimesCategory(categoryName: String): List<AnimesCategoryResponse> {
        return networkBuilder.doRequest {
            service.getAnimesCategory(categoryName)
        }
    }

    override suspend fun getAnimeEp(animeId: String): List<AnimeEpResponse> {
        return networkBuilder.doRequest {
            service.getAnimeEp(animeId)
        }
    }

    override suspend fun getAnimeDetails(animeId: String): List<AnimeDetailsResponse> {
        return networkBuilder.doRequest {
            service.getAnimeDetails(animeId)
        }
    }
}