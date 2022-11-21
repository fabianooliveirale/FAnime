package com.example.video.api

import com.example.video.model.VideoModelResponse

interface VideoRepository {
     suspend fun getLastReleases(episodesId: String) : List<VideoModelResponse>
}