package com.example.model

data class AnimeModel(
    var id: String? = null,
    var name: String? = null,
    var episodes: ArrayList<EpisodeModel> = arrayListOf(),
    var coverImage: String? = null,
    var categoryDescription: String? = null,
    var categoryGenres: String? = null,
    var year: String? = null,
) {
    fun fromAnimeDetailsResponse(animeDetailsResponse: AnimeDetailsResponse): AnimeModel {
        this.id = animeDetailsResponse.id
        this.name = animeDetailsResponse.name
        this.coverImage = animeDetailsResponse.categoryImage
        this.categoryDescription = animeDetailsResponse.categoryDescription
        this.categoryGenres = animeDetailsResponse.categoryGenres
        this.year = animeDetailsResponse.year
        return this
    }

    fun fromAnimeModel(animeModel: AnimeModel): AnimeModel {
        animeModel.id?.let {
            this.id = it
        }
        animeModel.name?.let {
            this.name = it
        }
        animeModel.coverImage?.let {
            this.coverImage = it
        }
        animeModel.categoryDescription?.let {
            this.categoryDescription = it
        }
        animeModel.year?.let {
            this.year = it
        }
        animeModel.episodes.forEach {  episodeModel ->
            val searchIndex = this.episodes.indexOfFirst { it.id == episodeModel.id }
            if (searchIndex >= 0) {
                this.episodes[searchIndex].fromEpisodeModel(episodeModel)
            } else {
                this.episodes.add(episodeModel)
            }
        }
        return this
    }

    fun addAllEpisodeFromAnimeEpResponse(animeEpResponse: List<AnimeEpResponse>) {
        animeEpResponse.forEach { response ->
            val index =
                this.episodes.indexOfFirst { episode -> episode.id == response.videoId } ?: -1
            if (index > 0) {
                this.episodes[index].fromAnimeEpResponse(response)
            } else {
                this.episodes.add(EpisodeModel().fromAnimeEpResponse(response))
            }
        }
    }

    fun addAllEpisodeFromVideoModelResponse(videoModelResponse: List<VideoModelResponse>) {
        videoModelResponse.forEach { response ->
            val index =
                this.episodes.indexOfFirst { episode -> episode.id == response.videoId }
            if (index > 0) {
                val ep = this.episodes[index]
                this.episodes[index] = ep.fromVideoModelResponse(response)
            } else {
                this.episodes.add(EpisodeModel().fromVideoModelResponse(response))
            }
        }
    }
}