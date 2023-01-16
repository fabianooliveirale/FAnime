package com.example.home.categories

import androidx.lifecycle.ViewModel
import com.example.home.EnumCategories
import com.example.router.Router
import com.example.screen_resources.ShowLoading

class CategoriesViewModel(
    private val loading: ShowLoading,
    private val router: Router,
): ViewModel() {

    val categoriesList: ArrayList<String> = ArrayList()

    init {
        categoriesList.addAll(ArrayList(EnumCategories.values().toList()).map { it.name })
    }

    fun getRouter() = router
    fun getShowLoading() = loading
}