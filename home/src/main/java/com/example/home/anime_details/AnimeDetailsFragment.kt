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
import com.example.network.NetworkResources
import com.example.screen_resources.isInt
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
        viewModel.animeEpResponseLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResources.Loading -> {
                    val teste = "loading"
                }
                is NetworkResources.Succeeded -> {
                    viewModel.animeEp = it.data
                    it.data.forEach { anime ->
                        val splitTitle = anime.title?.split(" ")
                        val special = if (splitTitle?.contains("Especial") == true) "Especial - " else ""

                        val epNumber = anime.title?.split(" ")?.last()?.replaceFirst("^0*".toRegex(), "")
                        anime.epNumber = if(epNumber?.isInt() == true) epNumber.toInt() else 0

                        val text = "${special}Episódio: $epNumber"
                        anime.title = text
                    }

                    binding.recyclerView.adapter =
                        AnimeDetailsAdapter(it.data.sortedBy { it.epNumber }.reversed()) { videoId ->
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