package com.example.home.anime_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dao.SharedPref
import com.example.home.api.HomeRepository
import com.example.model.*
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.SingleLiveEvent
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String,
    private val dao: SharedPref
) : ViewModel() {

    private val _animeDetailsMutableLiveData: SingleLiveEvent<NetworkResources<List<AnimeDetailsResponse>>> =
        SingleLiveEvent()
    val animeDetailsLiveData: LiveData<NetworkResources<List<AnimeDetailsResponse>>> =
        _animeDetailsMutableLiveData

    private val _animeEpResponsesMutableLiveData: SingleLiveEvent<NetworkResources<List<AnimeEpResponse>>> =
        SingleLiveEvent()
    val animeEpResponseLiveData: LiveData<NetworkResources<List<AnimeEpResponse>>> =
        _animeEpResponsesMutableLiveData

    fun getAnimeDetails(animeId: String) {
        viewModelScope.launch {
            networkScope.launch(_animeDetailsMutableLiveData) {
                repository.getAnimeDetails(animeId)
            }
        }
    }

    fun getAnimeEp(animeId: String) {
        viewModelScope.launch {
            networkScope.launch(_animeEpResponsesMutableLiveData) {
                repository.getAnimeEp(animeId)
            }
        }
    }

    fun getAll(animeId: String) {
        viewModelScope.launch {
            Log.d("getAllRequest", "getAllRequest - FIRST")
            networkScope.launch(_animeDetailsMutableLiveData) {
                repository.getAnimeDetails(animeId)
            }
            Log.d("getAllRequest", "getAllRequest - SECOND")
            networkScope.launch(_animeEpResponsesMutableLiveData) {
                repository.getAnimeEp(animeId)
            }
        }
    }

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
    fun getDao() = dao
}