package com.example.home.anime_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.home.R
import com.example.home.animes_category.AnimesCategoryAdapter
import com.example.home.animes_category.AnimesCategoryFragmentArgs
import com.example.home.databinding.FragmentAnimeDetailsBinding
import com.example.home.databinding.FragmentNewVideosBinding
import com.example.home.new_videos.NewVideosViewModel
import com.example.network.NetworkResources
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AnimeDetailsFragment : Fragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<AnimeDetailsViewModel>()
    private val args: AnimeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getAnimeDetails()
        initLiveDataAnimeDetails()
        initLiveDataAnimeEp()
    }

    private fun initToolbar() {
        binding.toolbar.setLeftIconClick {
            activity?.onBackPressed()
        }
    }

    private fun getAnimeDetails() {
        val animeId = args.animeId
        if (animeId == null) {
            Log.d("error", "valueNull")
            return
        }

        if (animeId == "") {
            Log.d("error", "valueNull")
            return
        }
        viewModel.getAnimeDetails(animeId)
        viewModel.getAnimeEp(animeId)
    }

    private fun initLiveDataAnimeDetails() {
        viewModel.animeDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    val teste = "loading"
                }
                is NetworkResources.Succeeded -> {
                    val data = it.data
                    val animeDetail = data.first()
                    binding.apply {
                        yearTextView.text = animeDetail.year
                        genreTextView.text = animeDetail.categoryGenres
                        descriptionTextView.text = animeDetail.categoryDescription
                        titleTextView.text = animeDetail.name
                        toolbar.title = animeDetail.name

                        val imageUrl = "${viewModel.getBaseImageUrl()}${animeDetail.categoryImage}"
                        Glide
                            .with(this.root.context)
                            .load(imageUrl)
                            .centerCrop()
                            .placeholder(com.example.screen_resources.R.drawable.progress_loading)
                            .into(imageView)
                    }
                }
                is NetworkResources.Failure -> {
                    val teste = "error"
                }
            }
        }
    }

    private fun initLiveDataAnimeEp() {
        viewModel.animeEpResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    val teste = "loading"
                }
                is NetworkResources.Succeeded -> {
                    viewModel.animeEp = it.data
                    binding.recyclerView.adapter =
                        AnimeDetailsAdapter(it.data.reversed()) { videoId ->
                            viewModel.getRouter().goToVideo(binding.root, videoId)
                        }
                }
                is NetworkResources.Failure -> {
                    val teste = "error"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}