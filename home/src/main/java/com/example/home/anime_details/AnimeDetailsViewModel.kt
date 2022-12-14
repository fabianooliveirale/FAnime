package com.example.home.anime_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.api.HomeRepository
import com.example.model.AnimeDetailsResponse
import com.example.model.AnimeEpResponse
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel() {

    private val _animeDetailsMutableLiveData: MutableLiveData<NetworkResources<List<com.example.model.AnimeDetailsResponse>>> =
        MutableLiveData()
    val animeDetailsLiveData: LiveData<NetworkResources<List<com.example.model.AnimeDetailsResponse>>> =
        _animeDetailsMutableLiveData

    private val _animeEpResponsesMutableLiveData: MutableLiveData<NetworkResources<List<com.example.model.AnimeEpResponse>>> =
        MutableLiveData()
    val animeEpResponseLiveData: LiveData<NetworkResources<List<com.example.model.AnimeEpResponse>>> =
        _animeEpResponsesMutableLiveData

    var animeEp: List<com.example.model.AnimeEpResponse>? = null

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

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
}