package com.example.home.all_watching

import androidx.lifecycle.ViewModel
import com.example.dao.SharedPref
import com.example.model.WatchingEp
import com.example.router.Router
import com.example.screen_resources.ShowLoading
import com.example.screen_resources.extensions.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AllWatchingViewModel(
    private val loading: ShowLoading,
    private val sharedPref: SharedPref,
    private val router: Router,
    private val baseImageUrl: String
) : ViewModel() , CoroutineScope {

    override val coroutineContext = Dispatchers.Main + Job()

    var list: ArrayList<WatchingEp> = ArrayList()
    private var _searchCallList: (ArrayList<WatchingEp>) -> Unit = {}

    val textSearchChange: (String?) -> Unit = debounce(
        350L,
        this
    ) { searchString ->
        if(searchString == "" || (searchString?.count() ?: 0) <= 2) {
            _searchCallList(list)
            return@debounce
        }
        val searchedList = list.filter { it.title?.uppercase()?.startsWith(searchString?.uppercase() ?: "") ?: false }
        _searchCallList(ArrayList(searchedList))
    }

    fun setSearchCallBack(callBack: (ArrayList<WatchingEp>) -> Unit) {
        _searchCallList = callBack
    }

    fun getSharedPref() = sharedPref
    fun getImageUrl() = baseImageUrl
    fun getRouter() = router
    fun getShowLoading() = loading
}