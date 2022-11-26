package com.example.video

interface PlayerInterface {
    fun pause()
    fun play()
    fun resume()
    fun save()
    fun updatePositon()
    fun updateTime()
    fun updateIsWatch()
    fun updateLayoutTitle()
    fun updateEpisodeCoverImage()
    fun updateEpisode(animeId: String, episodeId: String)
    fun next()
    fun previous()
    fun refreshEpisode()
}