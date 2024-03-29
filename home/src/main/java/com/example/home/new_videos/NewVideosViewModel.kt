package com.example.home.new_videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.api.HomeRepository
import com.example.model.NewVideosResponse
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.ShowLoading
import kotlinx.coroutines.launch

class NewVideosViewModel(
    private val loading: ShowLoading,
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router,
    private val baseImageUrl: String
): ViewModel() {

    private val _newVideosMutableLiveData: MutableLiveData<NetworkResources<List<NewVideosResponse>>> = MutableLiveData()
    val newVideosLiveData: LiveData<NetworkResources<List<NewVideosResponse>>> = _newVideosMutableLiveData

    var newVideosData: List<NewVideosResponse>? = null

    fun getNewVideos() {
        viewModelScope.launch {
            networkScope.launch(_newVideosMutableLiveData) {
                repository.getNewVideos()
            }
        }
    }

    fun getRouter() = router
    fun getBaseImageUrl() = baseImageUrl
    fun getShowLoading() = loading
}