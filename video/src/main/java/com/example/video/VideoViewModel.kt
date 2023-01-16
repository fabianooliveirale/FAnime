package com.example.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dao.SharedPref
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.screen_resources.Loop
import com.example.video.api.VideoRepository
import com.example.video.model.VideoModelResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VideoViewModel(
    private val repository: VideoRepository,
    private val networkScope: NetworkScope,
    private val loop: Loop,
    private val sharedPref: SharedPref,
) : ViewModel(), CoroutineScope {

    override val coroutineContext = Dispatchers.IO + Job()

    private val _videoModelMutableLiveData: MutableLiveData<NetworkResources<List<VideoModelResponse>>> =
        MutableLiveData()
    val videoModelLiveData: LiveData<NetworkResources<List<VideoModelResponse>>> =
        _videoModelMutableLiveData

    private val _nextMutableLiveData: MutableLiveData<NetworkResources<List<VideoModelResponse>>> =
        MutableLiveData()
    val nextLiveData: LiveData<NetworkResources<List<VideoModelResponse>>> =
        _nextMutableLiveData

    private val _previousMutableLiveData: MutableLiveData<NetworkResources<List<VideoModelResponse>>> =
        MutableLiveData()
    val previousLiveData: LiveData<NetworkResources<List<VideoModelResponse>>> =
        _previousMutableLiveData

    fun getVideo(videoId: String) {
        viewModelScope.launch {
            networkScope.launch(_videoModelMutableLiveData) {
                repository.getLastReleases(videoId)
            }
        }
    }

    fun nextEp(episodeId: String, catId: String) {
        viewModelScope.launch {
            networkScope.launch(_nextMutableLiveData) {
                repository.nextEp(episodeId, catId)
            }
        }
    }

    fun previousEp(episodeId: String, catId: String) {
        viewModelScope.launch {
            networkScope.launch(_previousMutableLiveData) {
                repository.previouEp(episodeId, catId)
            }
        }
    }
    fun getLoop() = loop
    fun getSharedPref() = sharedPref
}