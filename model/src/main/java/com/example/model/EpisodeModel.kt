package com.example.model

import java.util.*

class EpisodeModel(
    var id: String? = null,
    var animeId: String? = null,
    var watchingTime: Date? = null,
    var position: Int? = null,
    var videoUrlSD: String? = null,
    var videoUrlHD: String? = null,
    var watched: Boolean = false,
    var number: Int? = null,
    var numberName: String? = null
) {
    fun fromAnimeEpResponse(animeEpResponse: AnimeEpResponse): EpisodeModel {
        val epNumber =
            animeEpResponse.title?.split(" ")?.last()?.replaceFirst("^0*".toRegex(), "")
        this.id = animeEpResponse.videoId
        this.number = if (epNumber?.isInt() == true) epNumber.toInt() else 0
        this.numberName = epNumber
        this.animeId = animeEpResponse.categoryId
        return this
    }

    fun fromEpisodeModel(episodeModel: EpisodeModel): EpisodeModel {
        episodeModel.id?.let {
            this.id = it
        }
        episodeModel.watchingTime?.let {
            this.watchingTime = it
        }
        episodeModel.animeId?.let {
            this.animeId = it
        }
        episodeModel.position?.let {
            this.position = it
        }
        episodeModel.videoUrlSD?.let {
            this.videoUrlSD = it
        }
        episodeModel.videoUrlHD?.let {
            this.videoUrlHD = it
        }
        episodeModel.number?.let {
            this.number = it
        }
        episodeModel.numberName?.let {
            this.numberName = it
        }
        return this
    }

    fun fromVideoModelResponse(videoModelResponse: VideoModelResponse): EpisodeModel {
        this.videoUrlSD = videoModelResponse.location
        this.videoUrlHD = videoModelResponse.locationSd
        this.animeId = videoModelResponse.animeId
        this.id = videoModelResponse.videoId
        return this
    }
}


fun String.isInt(): Boolean = try {
    toInt()
    true
} catch (e: NumberFormatException) {
    false
}