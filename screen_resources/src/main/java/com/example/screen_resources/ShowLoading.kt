package com.example.screen_resources

class ShowLoading {
    private var loadingCallBack: (Boolean) -> Unit = {}

    fun setLoadingCallBack(callBack: (Boolean) -> Unit) {
        loadingCallBack = callBack
    }

    fun showLoading() {
        loadingCallBack(true)
    }

    fun hideLoading() {
        loadingCallBack(false)
    }
}