package com.example.home.categories

import androidx.lifecycle.ViewModel
import com.example.home.EnumCategories
import com.example.router.Router

class CategoriesViewModel(
    private val router: Router,
): ViewModel() {

    val categoriesList: ArrayList<String> = ArrayList()

    init {
        categoriesList.addAll(ArrayList(EnumCategories.values().toList()).map { it.name })
    }

    fun getRouter() = router
}