package com.example.home.api

import com.example.dao.SharedPref
import com.example.model.*
import com.example.network.NetworkBuilder

class HomeRepositoryImpl(
    private val networkBuilder: NetworkBuilder,
    private val service: HomeService,
    private val dao: SharedPref
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
        val list = networkBuilder.doRequest {
            service.getAnimeDetails(animeId)
        }

        list.forEach {
            dao.saveAnime(AnimeModel().fromAnimeDetailsResponse(it))
        }

        return list
    }

    override suspend fun getSearchAnime(searchValue: String): List<SearchResponse> {
        return networkBuilder.doRequest {
            service.getSearchAnime(searchValue)
        }
    }
}