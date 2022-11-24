package com.example.model

import com.google.gson.annotations.SerializedName

data class AnimeDetailsResponse(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("category_name")
    var name: String? = null,
    @SerializedName("category_image")
    var categoryImage: String? = null,
    @SerializedName("category_description")
    var categoryDescription: String? = null,
    @SerializedName("category_genres")
    var categoryGenres: String? = null,
    @SerializedName("ano")
    var year: String? = null
)