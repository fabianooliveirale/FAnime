package com.example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.home.categories.CategoriesFragment
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.new_videos.NewVideosFragment
import com.example.home.watched_videos.WatchedVideosFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val watchedVideosFragment: Fragment = WatchedVideosFragment()
    private val newVideosFragment: Fragment = NewVideosFragment()
    private val categoriesFragment: Fragment = CategoriesFragment()
    private var oldValueNavigationId = R.id.home_last_watched

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomNavigationView()
        changeFragment(watchedVideosFragment, R.id.home_last_watched)
    }

    private fun initBottomNavigationView() {
        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home_last_watched -> {
                        changeFragment(watchedVideosFragment, item.itemId)
                    }
                    R.id.home_news -> {
                        changeFragment(newVideosFragment, item.itemId)
                    }
                    R.id.home_categories -> {
                        changeFragment(categoriesFragment, item.itemId)
                    }
                }
                return@setOnItemSelectedListener true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun changeFragment(fragment: Fragment, itemId: Int) {
        val result = checkViewId(itemId)
        oldValueNavigationId = itemId

        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(
            getTransitionAnimIn(result), getTransitionAnimOut(result)
        )
        fragmentManager?.addOnBackStackChangedListener { }
        fragmentTransaction?.replace(binding.fragmentContainer.id, fragment, "transitionAnime")
        fragmentTransaction?.addToBackStack("transitionAnime")
        fragmentTransaction?.commit()
    }

    private fun checkViewId(itemId: Int): Boolean {
        return when (oldValueNavigationId) {
            R.id.home_last_watched -> true
            R.id.home_news -> itemId != R.id.home_last_watched
            R.id.home_categories -> false
            else -> false
        }
    }

    private fun getTransitionAnimIn(result: Boolean) =
        if (result) com.example.screen_resources.R.anim.animation_enter else com.example.screen_resources.R.anim.animation_pop_enter

    private fun getTransitionAnimOut(result: Boolean) =
        if (result) com.example.screen_resources.R.anim.animation_exit else com.example.screen_resources.R.anim.animation_pop_exit
}