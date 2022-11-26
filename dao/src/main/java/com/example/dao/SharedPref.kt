package com.example.dao

import android.content.Context
import android.util.Log
import com.example.model.AnimeModel
import com.example.model.EpisodeModel
import com.example.model.VideoModelResponse
import com.example.model.WatchingEp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPref(private val context: Context, private val gson: Gson) {

    enum class SharedPrefEnum {
        ANIME, SHARED_PREF_FANIME, WATCHED_EP
    }

    fun saveAnime(anime: AnimeModel) {
        val animes = getAnimes()
        val index = animes.indexOfFirst { it.id == anime.id }
        if (index >= 0) {
            animes[index].fromAnimeModel(anime)
        } else {
            animes.add(anime)
        }
        with(getSharedPref().edit()) {
            putString(SharedPrefEnum.ANIME.name, modelToJson(animes))
            commit()
        }
    }

    fun saveEpisode(response: EpisodeModel) {
        val animeDao = getAnimeById(response.animeId ?: "")
        val episode = animeDao?.episodes?.first { episode -> episode.id == response.id }
        if (episode == null) {
            animeDao?.episodes?.add(response)
        } else {
            val index = animeDao.episodes.indexOfFirst { response.id == it.id }
            animeDao.episodes[index].fromEpisodeModel(response)
            saveAnime(animeDao)
        }

        animeDao?.let {
            saveAnime(it)
        }
    }

    fun saveAllEpisode(videos: ArrayList<EpisodeModel>) {
        val animeDao = getAnimeById(videos.first().animeId ?: "")
        videos.forEach { response ->
            val index = animeDao?.episodes?.indexOfFirst { response.id == it.id } ?: -1
            if (index > 0) {
                animeDao?.let {
                    it.episodes[index].fromEpisodeModel(response)
                }
            } else {
                animeDao?.episodes?.add(response)
            }
        }
        animeDao?.let {
            saveAnime(it)
        }
    }


    fun getAnimeById(id: String): AnimeModel? {
        return try {
            getAnimes().first { it.id == id }
        } catch (e: Exception) {
            null
        }
    }

    fun getEpisodeById(animeId: String, episodeId: String): EpisodeModel? {
        return try {
            return getAnimes().first { it.id == animeId }.episodes.first { it.id == episodeId }
        } catch (e: Exception) {
            null
        }
    }

    fun getAllWatchedEpisode(): List<EpisodeModel> {
        return try {
            return getAnimes().flatMap { it.episodes }.filter { it.watched }
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    fun getAnimeEpisodes(animeId: String): ArrayList<EpisodeModel>? {
        return try {
            return getAnimeById(animeId)?.episodes
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    fun getAnimes(): ArrayList<AnimeModel> {
        return try {
            val valueResult =
                getSharedPref().getString(SharedPrefEnum.ANIME.name, "") ?: return arrayListOf()
            jsonToArrayList(valueResult)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    fun saveWatchingEp(animeEp: WatchingEp) {
        try {
            val list = getWatchingEp()
            val item = list.find { it.animeId == animeEp.animeId && it.epId == animeEp.epId }
            val index = if (item != null) list.indexOf(item) else -1
            if (index < 0) list.add(animeEp) else list[index] = animeEp
            val takeList = list.take(10).sortedByDescending { it.time }
            with(getSharedPref().edit()) {
                putString(SharedPrefEnum.WATCHED_EP.name, modelToJson(takeList))
                commit()
            }
        } catch (e: Exception) {
            Log.d("SharedPref", "saveWatchingEp - Save ERRO")
        }
    }

    fun getWatchingEp(): ArrayList<WatchingEp> {
        return try {
            val valueResult = getSharedPref().getString(SharedPrefEnum.WATCHED_EP.name, "") ?: ""
            jsonToArrayList(valueResult)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    private fun <T> modelToJson(model: T): String = gson.toJson(model)
    private inline fun <reified T> jsonToArrayList(value: String): ArrayList<T> = gson.fromJson(
        value,
        object : TypeToken<ArrayList<T?>?>() {}.type
    ) as ArrayList<T>

    private inline fun <reified T> jsonToObject(value: String): T = gson.fromJson(
        value,
        object : TypeToken<T?>() {}.type
    ) as T

    private fun getSharedPref() =
        context.getSharedPreferences(SharedPrefEnum.SHARED_PREF_FANIME.name, Context.MODE_PRIVATE)
}