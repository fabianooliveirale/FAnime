package com.example.home.animes_category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.home.databinding.FragmentAnimesCategoryBinding
import com.example.home.databinding.FragmentCategoriesBinding
import com.example.network.NetworkResources
import com.example.screen_resources.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AnimesCategoryFragment : BaseFragment() {

    private var _binding: FragmentAnimesCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: AnimesCategoryFragmentArgs by navArgs()
    private val viewModel by sharedViewModel<AnimesCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimesCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAnimesCategory()
        initLiveData()
        initToolbar()
        binding.refreshView.setOnClickListener {
            getAnimesCategory()
        }
    }

    private fun initToolbar() {
        binding.toolbar.setLeftIconClick {
            activity?.onBackPressed()
        }
    }

    private fun getAnimesCategory() {
        val categoryName = args.categoryName
        if (categoryName == null) {
            Log.d("error", "valueNull")
            return
        }

        if (categoryName == "") {
            Log.d("error", "valueNull")
            return
        }
        binding.toolbar.title = categoryName
        viewModel.getAnimesCategory(categoryName)
    }

    private fun initLiveData() {
        viewModel.animesCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    viewModel.getShowLoading().showLoading()
                }
                is NetworkResources.Succeeded -> {
                    viewModel.getShowLoading().hideLoading()
                    viewModel.animesCategoryData = it.data
                    binding.recyclerView.adapter =
                        AnimesCategoryAdapter(it.data, viewModel.getBaseImageUrl()) { animeId ->
                            viewModel.getRouter().goToAnimeDetails(binding.root, animeId)
                        }
                    binding.refreshView.isGone = true
                }
                is NetworkResources.Failure -> {
                    viewModel.getShowLoading().hideLoading()
                    binding.refreshView.isGone = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}