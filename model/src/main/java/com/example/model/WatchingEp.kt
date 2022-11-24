package com.example.model

import java.util.Date

data class WatchingEp(
    var epId: String? = null,
    var animeId: String? = null,
    var title: String? = null,
    var position: Int? = null,
    var image: String? = null,
    var time: Date? = null
)