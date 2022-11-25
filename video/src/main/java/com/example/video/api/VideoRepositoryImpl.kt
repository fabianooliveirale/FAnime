package com.example.video.api

import com.example.dao.SharedPref
import com.example.model.EpisodeModel
import com.example.network.NetworkBuilder
import com.example.model.VideoModelResponse
import com.example.video.VideoViewModel

class VideoRepositoryImpl(
    private val networkBuilder: NetworkBuilder,
    private val service: VideoService,
    private val dao: SharedPref
) : VideoRepository {

    override suspend fun getLastReleases(episodesId: String): List<VideoModelResponse> {
        val list = networkBuilder.doRequest {
            service.getLastReleases(episodesId)
        }

        val episodeList = transformListToEpisodeList(list)
        dao.saveAllEpisode(episodeList)

        return list
    }

    override suspend fun nextEp(episodesId: String, catId: String): List<VideoModelResponse> {
        val list = networkBuilder.doRequest {
            service.nextEp(episodesId, catId)
        }

        val episodeList = transformListToEpisodeList(list)
        dao.saveAllEpisode(episodeList)

        return list
    }

    override suspend fun previouEp(episodesId: String, catId: String): List<VideoModelResponse> {
        val list = networkBuilder.doRequest {
            service.previousEp(episodesId, catId)
        }

        val episodeList = transformListToEpisodeList(list)
        dao.saveAllEpisode(episodeList)

        return list
    }


    private fun transformListToEpisodeList(videos: List<VideoModelResponse>): ArrayList<EpisodeModel> {
        return ArrayList(videos.map { response ->
            EpisodeModel().fromVideoModelResponse(response)
        })
    }
}