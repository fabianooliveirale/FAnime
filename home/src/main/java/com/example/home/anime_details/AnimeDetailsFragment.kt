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
    }

    private fun getAnimeFromRepository() {
        args.animeId.let { animeId ->
            anime = viewModel.getDao().getAnimeById(animeId)
            if (episodeId != null && anime != null) {
                navigateToVideo(animeId, episodeId ?: "")
                return
            }

            if (episodeId != null) {
                viewModel.getAll(animeId)
                return
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
                    binding.apply {
                        val response = it.data.first()
                        anime = viewModel.getDao().getAnimeById(response.id ?: "")
                        if (anime == null) {
                            anime = AnimeModel().fromAnimeDetailsResponse(response)
                        } else {
                            anime?.fromAnimeDetailsResponse(response)
                        }

                        anime?.let { animeModel ->
                            viewModel.getDao().saveAnime(animeModel)
                        }

                        Log.d("getAllRequest", "getAllRequest - FIRST - SUCCESS")
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
                is NetworkResources.Failure -> {
                    Log.d("getAllRequest", "getAllRequest - FIRST - FAILURE")
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
                        binding.recyclerView.adapter =
                            AnimeDetailsAdapter(it) { item ->
                                navigateToVideo(item.animeId ?: "", item.id ?: "")
                            }

                        if (episodeId != null) {
                            navigateToVideo(anime.id ?: "", episodeId ?: "")
                        }
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
            this,
            animeId,
            episodeId
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}