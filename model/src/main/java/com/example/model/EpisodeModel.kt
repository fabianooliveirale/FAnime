package com.example.model

import androidx.core.net.toUri
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
    var numberName: String? = null,
    var fullName: String? = null,
    var name: String? = null,
    var converImage: String? = null
) {
    fun fromAnimeEpResponse(animeEpResponse: AnimeEpResponse): EpisodeModel {
        val animeNameSplit = animeEpResponse.title?.split(" ") ?: arrayListOf()
        val epNumber =
            animeNameSplit.last().replaceFirst("^0*".toRegex(), "")

        this.id = animeEpResponse.videoId
        this.number = if (epNumber.isInt()) epNumber.toInt() else 0
        this.numberName = epNumber
        this.animeId = animeEpResponse.categoryId
        this.fullName = animeEpResponse.title
        this.name = if (animeNameSplit.count() > 2) animeNameSplit.take(animeNameSplit.count() - 2)
            .joinToString(" ") else animeNameSplit.joinToString(" ")
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

    fun getVideoUri() = videoUrlHD?.toUri() ?: videoUrlSD?.toUri()
}


fun String.isInt(): Boolean = try {
    toInt()
    true
} catch (e: NumberFormatException) {
    false
}