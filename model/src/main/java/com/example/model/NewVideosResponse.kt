package com.example.model

import com.google.gson.annotations.SerializedName

data class NewVideosResponse(
    @SerializedName("video_id")
    var videoId: String? = null,
    @SerializedName("category_id")
    var categoryId: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("category_image")
    var categoryImage: String? = null
)