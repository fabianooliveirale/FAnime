package com.example.home.animes_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.api.HomeRepository
import com.example.home.model.AnimesCategoryResponse
import com.example.home.model.NewVideosResponse
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import kotlinx.coroutines.launch

class AnimesCategoryViewModel(
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel() {

    private val _animesCategoryMutableLiveData: MutableLiveData<NetworkResources<List<AnimesCategoryResponse>>> =
        MutableLiveData()
    val animesCategoryLiveData: LiveData<NetworkResources<List<AnimesCategoryResponse>>> =
        _animesCategoryMutableLiveData

    var animesCategoryData: List<AnimesCategoryResponse>? = null

    fun getAnimesCategory(categoryName: String) {
        viewModelScope.launch {
            networkScope.launch(_animesCategoryMutableLiveData) {
                repository.getAnimesCategory(categoryName)
            }
        }
    }

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
}