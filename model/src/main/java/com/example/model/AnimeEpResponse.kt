package com.example.model

import com.google.gson.annotations.SerializedName

data class AnimeEpResponse(
    @SerializedName("video_id")
    var videoId: String? = null,
    @SerializedName("category_id")
    var categoryId: String? = null,
    @SerializedName("title")
    var title: String? = null,
    var isWatchingEp: Boolean = false
)



