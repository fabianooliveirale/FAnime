package com.example.home.anime_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dao.SharedPref
import com.example.home.api.HomeRepository
import com.example.model.AnimeDetailsResponse
import com.example.model.AnimeEpResponse
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.ShowLoading
import com.example.screen_resources.ViewAnimation
import com.example.screen_resources.extensions.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val animation: ViewAnimation,
    private val loading: ShowLoading,
    private val sharedPref: SharedPref,
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel() , CoroutineScope {

    override val coroutineContext = Dispatchers.Main + Job()

    private val _animeDetailsMutableLiveData: MutableLiveData<NetworkResources<List<AnimeDetailsResponse>>> =
        MutableLiveData()
    val animeDetailsLiveData: LiveData<NetworkResources<List<AnimeDetailsResponse>>> =
        _animeDetailsMutableLiveData


    private var _searchCallList: (ArrayList<AnimeEpResponse>) -> Unit = {}

    private val _animeEpResponsesMutableLiveData: MutableLiveData<NetworkResources<List<AnimeEpResponse>>> =
        MutableLiveData()
    val animeEpResponseLiveData: LiveData<NetworkResources<List<AnimeEpResponse>>> =
        _animeEpResponsesMutableLiveData

    var listEp: ArrayList<AnimeEpResponse> = ArrayList()

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

    val textSearchChange: (String?) -> Unit = debounce(
        350L,
        this
    ) { searchString ->
        val searchedList = listEp.filter { it.title?.uppercase()?.contains(searchString?.uppercase() ?: "") ?: false }
        _searchCallList(ArrayList(searchedList))
    }

    fun setSearchCallBack(callBack: (ArrayList<AnimeEpResponse>) -> Unit) {
        _searchCallList = callBack
    }

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
    fun getSharedPref() = sharedPref
    fun getShowLoading() = loading
    fun getAnimationView() = animation
}