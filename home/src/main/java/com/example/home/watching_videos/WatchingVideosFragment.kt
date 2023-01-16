package com.example.home.watching_videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.home.HomeViewModel
import com.example.home.databinding.FragmentWatchingVideosBinding
import com.example.screen_resources.BaseFragment
import com.example.video.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WatchingVideosFragment : BaseFragment() {

    private var _binding: FragmentWatchingVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<WatchingViewModel>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private var adapterEp: WatchingEpAdapter? = null
    private var adapterAnime: WatchingEpAdapter? = null
    private var adapterFavorite: WatchingEpAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchingVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initTopMenu()
    }

    private fun initTopMenu() {
        binding.apply {
            searchTopMenu.setOnClickListener {
                homeViewModel.searchCallBack()
            }
        }
    }

    private fun initAdapters() {
        adapterFavorite = WatchingEpAdapter(viewModel.getImageUrl()) {
            viewModel.getRouter().goToAnimeDetails(
                binding.root,
                it.animeId ?: ""
            )
        }

        adapterEp = WatchingEpAdapter(viewModel.getImageUrl(), true) {
            viewModel.getRouter().goToVideo(
                activity,
                PlayerActivity(),
                it.epId ?: "",
                it.animeId ?: "",
                it.title ?: "",
                it.image ?: "",
                it.position ?: 0
            )
        }

        adapterAnime = WatchingEpAdapter(viewModel.getImageUrl()) {
            viewModel.getRouter().goToAnimeDetails(
                binding.root,
                it.animeId ?: ""
            )
        }
    }

    override fun onResume() {
        super.onResume()
        updateFavorite()
        updateWatchingEpList()
        updateWatchingAnimeList()
    }

    private fun updateFavorite() {
        val list = viewModel.getSharedPref().getFavoriteEp()

        adapterFavorite?.replaceList(ArrayList(list))
        binding.recyclerFavorite.adapter = adapterFavorite

        binding.favoriteContainer.isGone = list.isEmpty()
    }

    private fun updateWatchingEpList() {
        val list = viewModel.getSharedPref().getWatchingEp()

        adapterEp?.replaceList(list)
        binding.recyclerViewEp.adapter = adapterEp

        binding.continueWatchingEpContainer.isGone = list.isEmpty()
    }

    private fun updateWatchingAnimeList() {
        val list = viewModel.getSharedPref().getWatchingEp().distinctBy { it.animeId }

        adapterAnime?.replaceList(ArrayList(list))
        binding.recyclerViewAnime.adapter = adapterAnime

        binding.continueWatchingAnimeContainer.isGone = list.isEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}