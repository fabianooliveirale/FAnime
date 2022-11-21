package com.example.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.NetworkResources
import com.example.network.NetworkScope
import com.example.video.api.VideoRepository
import com.example.video.model.VideoModelResponse
import kotlinx.coroutines.launch

class VideoViewModel(
    private val repository: VideoRepository,
    private val networkScope: NetworkScope
) : ViewModel() {

    private val _videoModelMutableLiveData: MutableLiveData<NetworkResources<List<VideoModelResponse>>> =
        MutableLiveData()
    val videoModelLiveData: LiveData<NetworkResources<List<VideoModelResponse>>> =
        _videoModelMutableLiveData

    fun getVideo(videoId: String) {
        viewModelScope.launch {
            networkScope.launch(_videoModelMutableLiveData) {
                repository.getLastReleases(videoId)
            }
        }
    }

}