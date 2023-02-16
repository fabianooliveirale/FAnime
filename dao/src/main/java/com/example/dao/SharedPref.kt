package com.example.dao

import android.content.Context
import android.util.Log
import com.example.model.WatchingEp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPref(private val context: Context, private val gson: Gson) {

    enum class SharedPrefEnum {
        SHARED_PREF_FANIME, WATCHED_EP, FAVORITE_ANIME
    }

    fun saveWatchingEp(animeEp: WatchingEp) {
        try {
            val list = getWatchingEp()
            val item = list.find { it.animeId == animeEp.animeId && it.epId == animeEp.epId }
            val index = if (item != null) list.indexOf(item) else -1
            if (index < 0) list.add(animeEp) else list[index] = animeEp
            val takeList = list.sortedByDescending { it.time }
            with(getSharedPref().edit()) {
                putString(SharedPrefEnum.WATCHED_EP.name, modelToJson(takeList))
                commit()
            }
        } catch (e: Exception) {
            Log.d("SharedPref", "saveWatchingEp - Save ERRO")
        }
    }

    fun saveFavoriteAnime(animeEp: WatchingEp) {
        try {
            val list = getFavoriteEp()
            val item = list.find { it.animeId == animeEp.animeId }
            val index = if (item != null) list.indexOf(item) else -1
            if (index < 0) list.add(animeEp) else list[index] = animeEp
            val takeList = list.sortedByDescending { it.time }
            with(getSharedPref().edit()) {
                putString(SharedPrefEnum.FAVORITE_ANIME.name, modelToJson(takeList))
                commit()
            }
        } catch (e: Exception) {
            Log.d("SharedPref", "saveWatchingEp - Save ERRO")
        }
    }

    fun removeFavoriteAnime(animeId: String) {
        try {
            val list = getFavoriteEp()
            val filterList = list.filter { it.animeId != animeId }
            val takeList = filterList.sortedByDescending { it.time }
            with(getSharedPref().edit()) {
                putString(SharedPrefEnum.FAVORITE_ANIME.name, modelToJson(takeList))
                commit()
            }
        } catch (e: Exception) {
            Log.d("SharedPref", "saveWatchingEp - Save ERRO")
        }
    }

    fun animeIsFavorite(animeId: String): Boolean {
        return try {
            val list = getFavoriteEp()
            !(list.none { it.animeId == animeId })
        } catch (e: Exception) {
            Log.d("SharedPref", "saveWatchingEp - Save ERRO")
            false
        }
    }

    fun getFavoriteEp(): ArrayList<WatchingEp> {
        return try {
            val valueResult = getSharedPref().getString(SharedPrefEnum.FAVORITE_ANIME.name, "") ?: ""
            jsonToArrayList(valueResult)
        } catch (e: Exception) {
            Log.d("SharedPref", "getWatchingEp - Get ERRO")
            arrayListOf()
        }
    }

    fun getWatchingEp(): ArrayList<WatchingEp> {
        return try {
            val valueResult = getSharedPref().getString(SharedPrefEnum.WATCHED_EP.name, "") ?: ""
            ArrayList(jsonToArrayList<WatchingEp>(valueResult).take(20))
        } catch (e: Exception) {
            Log.d("SharedPref", "getWatchingEp - Get ERRO")
            arrayListOf()
        }
    }

    fun getWatchingEpByAnime(animeId: String): ArrayList<WatchingEp> {
        return try {
            val valueResult = getSharedPref().getString(SharedPrefEnum.WATCHED_EP.name, "") ?: ""
            ArrayList(jsonToArrayList<WatchingEp>(valueResult).filter { it.animeId == animeId })
        } catch (e: Exception) {
            Log.d("SharedPref", "getWatchingEp - Get ERRO")
            arrayListOf()
        }
    }

    private fun <T> modelToJson(model: T): String = gson.toJson(model)
    private inline fun <reified T> jsonToArrayList(value: String): ArrayList<T> = gson.fromJson(
        value,
        object : TypeToken<ArrayList<T?>?>() {}.type
    ) as ArrayList<T>

    private fun getSharedPref() =
        context.getSharedPreferences(SharedPrefEnum.SHARED_PREF_FANIME.name, Context.MODE_PRIVATE)
}