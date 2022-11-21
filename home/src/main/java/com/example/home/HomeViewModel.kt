package com.example.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.home.api.HomeRepository
import com.example.home.model.NewVideosResponse
import com.example.network.NetworkScope
import com.example.network.NetworkResources
import com.example.router.Router
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    private val networkScope: NetworkScope,
    private val router: Router
): ViewModel() {


}