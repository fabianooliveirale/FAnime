package com.example.model

import com.google.gson.annotations.SerializedName

data class VideoModelResponse(
    @SerializedName("video_id")
    var videoId: String? = null,
    @SerializedName("category_id")
    var animeId: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("locationsd")
    var locationSd: String? = null,
)