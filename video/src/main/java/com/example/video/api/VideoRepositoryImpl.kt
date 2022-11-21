package com.example.video.api

import com.example.network.NetworkBuilder
import com.example.video.model.VideoModelResponse

class VideoRepositoryImpl(
    private val networkBuilder: NetworkBuilder,
    private val service: VideoService
) : VideoRepository {

    override suspend fun getLastReleases(episodesId: String): List<VideoModelResponse> {
       return networkBuilder.doRequest {
            service.getLastReleases(episodesId)
        }
    }
}