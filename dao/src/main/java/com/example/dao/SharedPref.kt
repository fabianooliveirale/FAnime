package com.example.dao

import android.content.Context
import android.util.Log
import com.example.model.WatchingEp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPref(private val context: Context, private val gson: Gson) {

    enum class SharedPrefEnum {
        SHARED_PREF_FANIME, WATCHED_EP
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