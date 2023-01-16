package com.example.screen_resources.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadFromGlide(imageUrl: String) {
    Glide
        .with(this.context)
        .load(imageUrl)
        .centerCrop()
        .error(com.example.screen_resources.R.drawable.ic_f_anime)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .placeholder(com.example.screen_resources.R.drawable.progress_loading)
        .into(this)
}