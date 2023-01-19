package com.example.video.api

import com.example.network.NetworkBuilder
import com.example.video.model.VideoModelResponse

class VideoRepositoryImpl(
    private val networkBuilder: NetworkBuilder,
    private val service: VideoService
) : VideoRepository {

    override suspend fun getAnimeDetails(animeId: String): List<com.example.model.AnimeDetailsResponse> {
        return networkBuilder.doRequest {
            service.getAnimeDetails(animeId)
        }
    }


    override suspend fun getLastReleases(episodesId: String): List<VideoModelResponse> {
       return networkBuilder.doRequest {
            service.getLastReleases(episodesId)
        }
    }

    override suspend fun nextEp(episodesId: String, catId: String): List<VideoModelResponse> {
        return networkBuilder.doRequest {
            service.nextEp(episodesId, catId)
        }
    }

    override suspend fun previouEp(episodesId: String, catId: String): List<VideoModelResponse> {
        return networkBuilder.doRequest {
            service.previousEp(episodesId, catId)
        }
    }
}