package com.example.home.anime_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.home.databinding.FragmentAnimeDetailsBinding
import com.example.model.AnimeModel
import com.example.model.EpisodeModel
import com.example.network.NetworkResources
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AnimeDetailsFragment : Fragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<AnimeDetailsViewModel>()
    private val args: AnimeDetailsFragmentArgs by navArgs()
    private var anime: AnimeModel? = null
    private var episodeId: String? = null
    private var adapter = AnimeDetailsAdapter { item ->
        navigateToVideo(item.animeId ?: "", item.id ?: "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        episodeId = args.episodeId
        getAnimeFromRepository()
        initToolbar()
        initLiveDataAnimeDetails()
        initLiveDataAnimeEp()
        binding.recyclerView.adapter = adapter
    }

    private fun getAnimeFromRepository() {
        args.animeId.let { animeId ->
            anime = viewModel.getDao().getAnimeById(animeId)
            if (anime?.episodes?.isNotEmpty() == true) {
                adapter.replace(anime?.episodes ?: arrayListOf())
                updateLayout()
            }
            viewModel.getAnimeDetails(animeId)
            viewModel.getAnimeEp(animeId)
        }
    }

    private fun initToolbar() {
        binding.toolbar.setLeftIconClick {
            activity?.onBackPressed()
        }
    }

    private fun initLiveDataAnimeDetails() {
        viewModel.animeDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    Log.d("getAllRequest", "getAllRequest - FIRST - LOADING")
                }
                is NetworkResources.Succeeded -> {
                    val response = it.data.first()
                    anime = viewModel.getDao().getAnimeById(response.id ?: "")
                    if (anime == null) {
                        anime = AnimeModel().fromAnimeDetailsResponse(response)
                    } else {
                        anime?.fromAnimeDetailsResponse(response)
                    }

                    anime?.let { animeModel ->
                        viewModel.getDao().saveAnime(animeModel)
                        updateLayout()
                    }
                }
                is NetworkResources.Failure -> {

                }
            }
        }
    }


    private fun initLiveDataAnimeEp() {
        viewModel.animeEpResponseLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResources.Loading -> {
                    Log.d("getAllRequest", "getAllRequest - SECOND - LOADING")
                }
                is NetworkResources.Succeeded -> {
                    Log.d("getAllRequest", "getAllRequest - SECOND - SUCCESS")
                    val anime = viewModel.getDao().getAnimeById(it.data.first().categoryId ?: "")
                    val episodes = anime?.episodes

                    it.data.forEach { response ->
                        val index =
                            episodes?.indexOfFirst { episode -> response.videoId == episode.id }
                                ?: -1
                        if (index > 0) {
                            episodes?.let {
                                it[index].fromAnimeEpResponse(response)
                            }
                        } else {
                            episodes?.add(EpisodeModel().fromAnimeEpResponse(response))
                        }
                    }

                    episodes?.let {
                        viewModel.getDao().saveAllEpisode(it)
                        adapter.replace(it)
                    }
                }
                is NetworkResources.Failure -> {
                    Log.d("getAllRequest", "getAllRequest - SECOND - FAILURE")
                }
            }
        }
    }

    private fun navigateToVideo(animeId: String, episodeId: String) {
        viewModel.getRouter().goToVideo(
            activity,
            animeId,
            episodeId
        )
    }

    private fun updateLayout() {
        binding.apply {
            yearTextView.text = anime?.year
            genreTextView.text = anime?.categoryGenres
            descriptionTextView.text = anime?.categoryDescription
            titleTextView.text = anime?.name
            toolbar.title = anime?.name

            val imageUrl = "${viewModel.getBaseImageUrl()}${anime?.coverImage}"
            Glide
                .with(this.root.context)
                .load(imageUrl)
                .centerCrop()
                .placeholder(com.example.screen_resources.R.drawable.progress_loading)
                .into(imageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}