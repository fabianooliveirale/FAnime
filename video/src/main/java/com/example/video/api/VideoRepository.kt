package com.example.video.api

import com.example.video.model.VideoModelResponse

interface VideoRepository {
     suspend fun getLastReleases(episodesId: String) : List<VideoModelResponse>
     suspend fun nextEp(episodesId: String, catId: String) : List<VideoModelResponse>
     suspend fun previouEp(episodesId: String, catId: String) : List<VideoModelResponse>
}