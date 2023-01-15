package com.example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.home.categories.CategoriesFragment
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.new_videos.NewVideosFragment
import com.example.home.search.SearchAnimeFragment
import com.example.home.watching_videos.WatchingVideosFragment
import com.example.home.watching_videos.WatchingViewModel
import com.example.screen_resources.components.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<HomeViewModel>()
    private val viewModeRoul by sharedViewModel<WatchingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomView.setSelectItemCallBack {
            viewModel.bottomViewIndex = it

            val selectedFragment = when (it) {
                BottomNavigationView.NEW_VIEW_INDEX -> NewVideosFragment()
                BottomNavigationView.CATEGORY_VIEW_INDEX -> CategoriesFragment()
                BottomNavigationView.SEARCH_VIEW_INDEX -> SearchAnimeFragment()
                else -> WatchingVideosFragment()
            }

            setFragment(selectedFragment)
        }
        binding.bottomView.index = viewModel.bottomViewIndex
    }

    private fun setFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(binding.bottomView.fragmentContainer.id, fragment)
        ft.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}