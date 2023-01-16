package com.example.screen_resources

import androidx.lifecycle.ViewModel

class BaseViewModel(
    private val loading: ShowLoading
): ViewModel() {
    fun getLoading() = loading
}