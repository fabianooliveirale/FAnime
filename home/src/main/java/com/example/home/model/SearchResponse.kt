package com.example.home.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("category_name")
    var categoryName: String? = null,
    @SerializedName("category_image")
    var categoryImage: String? = null
)