package com.example.home.anime_details

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.home.R
import com.example.home.databinding.FragmentAnimeDetailsBinding
import com.example.model.AnimeDetailsResponse
import com.example.model.WatchingEp
import com.example.network.NetworkResources
import com.example.screen_resources.extensions.loadFromGlide
import com.example.screen_resources.isInt
import com.example.video.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class AnimeDetailsFragment : Fragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<AnimeDetailsViewModel>()
    private val args: AnimeDetailsFragmentArgs by navArgs()
    private var anime: AnimeDetailsResponse? = null

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
        setFavorite()
        initLiveDataAnimeDetails()
        initLiveDataAnimeEp()
    }

    private fun setFavorite() {
        val isFavorite = viewModel.getSharedPref().animeIsFavorite(args.animeId ?: "")
        val drawable =
            if (isFavorite) com.example.screen_resources.R.drawable.ic_baseline_favorite else com.example.screen_resources.R.drawable.ic_baseline_favorite_border

        binding.toolbar.favoriteView.setOnClickListener {
            if (isFavorite) removeFavoriteAnime() else saveFavoriteAnime()
        }

        binding.toolbar.favoriteView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
        binding.toolbar.favoriteView.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                com.example.screen_resources.R.color.heart_color
            )
        )
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
                is NetworkResources.Loading -> {}
                is NetworkResources.Succeeded -> {
                    anime = it.data.first()
                    binding.toolbar.favoriteView.isGone = false
                    binding.apply {
                        yearTextView.text = anime?.year
                        genreTextView.text = anime?.categoryGenres
                        descriptionTextView.text = anime?.categoryDescription
                        titleTextView.text = anime?.name
                        toolbar.title = anime?.name

                        val imageUrl = "${viewModel.getBaseImageUrl()}${anime?.categoryImage}"
                        binding.imageView.loadFromGlide(imageUrl)
                    }
                }
                is NetworkResources.Failure -> {}
            }
        }
    }

    private fun initLiveDataAnimeEp() {
        viewModel.animeEpResponseLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResources.Loading -> {}
                is NetworkResources.Succeeded -> {
                    viewModel.animeEp = it.data
                    it.data.forEach { anime ->
                        val splitTitle = anime.title?.split(" ")
                        val special =
                            if (splitTitle?.contains("Especial") == true) "Especial - " else ""

                        val epNumber =
                            anime.title?.split(" ")?.last()?.replaceFirst("^0*".toRegex(), "")
                        anime.epNumber = if (epNumber?.isInt() == true) epNumber.toInt() else 0

                        anime.epNumberName = "${special}Episódio: $epNumber"
                    }

                    binding.recyclerView.adapter =
                        AnimeDetailsAdapter(it.data.sortedBy { it.epNumber }
                            .reversed()) { item ->

                            viewModel.getRouter().goToVideo(
                                activity,
                                PlayerActivity(),
                                item.videoId ?: "",
                                item.categoryId ?: "",
                                item.title ?: "",
                                anime?.categoryImage ?: ""
                            )
                        }
                }
                is NetworkResources.Failure -> {}
            }
        }
    }

    private fun saveFavoriteAnime() {
        val watched = WatchingEp(
            animeId = anime?.id,
            title = anime?.name,
            image = anime?.categoryImage,
            time = Date()
        )
        viewModel.getSharedPref().saveFavoriteAnime(watched)
        setFavorite()
    }

    private fun removeFavoriteAnime() {
        viewModel.getSharedPref().removeFavoriteAnime(anime?.id ?: "")
        setFavorite()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}