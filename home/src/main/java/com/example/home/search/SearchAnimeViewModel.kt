package com.example.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.api.HomeRepository
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.ShowLoading
import com.example.screen_resources.extensions.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchAnimeViewModel(
    private val loading: ShowLoading,
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main + Job()

    val textSearchChange: (String?) -> Unit = debounce(
        350L,
        this
    ) { searchString ->
        if(searchString == "" || (searchString?.count() ?: 0) <= 2) return@debounce
        getAnimesCategory(searchString ?: "")
    }

    private val _animesSearchMutableLiveData: MutableLiveData<NetworkResources<List<com.example.model.SearchResponse>>> =
        MutableLiveData()
    val animesCategoryLiveData: LiveData<NetworkResources<List<com.example.model.SearchResponse>>> =
        _animesSearchMutableLiveData

    var animesCategoryData: List<com.example.model.SearchResponse>? = null

    private fun getAnimesCategory(searchValue: String) {
        viewModelScope.launch {
            networkScope.launch(_animesSearchMutableLiveData) {
                repository.getSearchAnime(searchValue)
            }
        }
    }

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
    fun getShowLoading() = loading
}