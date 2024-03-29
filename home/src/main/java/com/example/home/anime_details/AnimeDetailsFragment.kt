package com.example.home.anime_details

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.example.home.databinding.FragmentAnimeDetailsBinding
import com.example.model.AnimeDetailsResponse
import com.example.model.WatchingEp
import com.example.network.NetworkResources
import com.example.screen_resources.BaseFragment
import com.example.screen_resources.extensions.loadFromGlide
import com.example.screen_resources.extensions.onTextChanged
import com.example.screen_resources.fromHtml
import com.example.video.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class AnimeDetailsFragment : BaseFragment() {

    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<AnimeDetailsViewModel>()
    private val args: AnimeDetailsFragmentArgs by navArgs()
    private var anime: AnimeDetailsResponse? = null
    private var adapter: AnimeDetailsAdapter? = null
    private var isNewEp: Boolean = true

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
        initAdapter()
        initOrderByView()
        initEditTextWatcher()
        binding.refreshView.setOnClickListener {
            getAnimeDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun initOrderByView() {
        binding.orderByView.setOnClickListener {
            isNewEp = !isNewEp

            val textOrder = if (isNewEp) "Novos" else "Antigos"

            binding.orderByView.text = "Ordenar por: $textOrder"

            adapter?.reversed()
        }
    }

    private fun initAdapter() {
        adapter = AnimeDetailsAdapter { item ->
            viewModel.getRouter().goToVideo(
                activity,
                PlayerActivity(),
                item.videoId ?: "",
                item.categoryId ?: "",
                item.title ?: "",
                anime?.categoryImage ?: ""
            )
        }

        binding.recyclerView.adapter = adapter
    }

    private fun initEditTextWatcher() {
        viewModel.setSearchCallBack {
            adapter?.replaceList(it)
            binding.messageImageView.isGone = it.isNotEmpty()
            if (!isNewEp) {
                adapter?.reversed()
            }
        }

        binding.clearButton.setOnClickListener {
            binding.editText.text.clear()
        }

        binding.editText.onTextChanged(viewModel.textSearchChange)
        binding.editText.onTextChanged {
            binding.clearButton.isGone = it.isEmpty()
            if (it == "") {
                adapter?.clear()
            }
        }
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
                is NetworkResources.Loading -> {
                    viewModel.getShowLoading().showLoading()
                }
                is NetworkResources.Succeeded -> {
                    viewModel.getShowLoading().hideLoading()
                    anime = it.data.first()
                    binding.toolbar.favoriteView.isGone = false
                    binding.apply {
                        yearTextView.text = anime?.year
                        genreTextView.text = anime?.categoryGenres
                        descriptionTextView.text = anime?.categoryDescription?.fromHtml()
                        titleTextView.text = anime?.name
                        toolbar.title = anime?.name

                        viewModel.getAnimationView().slideInRight(binding.toolbar)
                        viewModel.getAnimationView().fadeInDown(binding.titleTextView)
                        viewModel.getAnimationView().fadeInRight(binding.yearTextView)
                        viewModel.getAnimationView().fadeInRight(binding.genreTextView)
                        viewModel.getAnimationView().slideInRight(binding.editTextContainer)
                        viewModel.getAnimationView().slideInLeft(binding.orderByView)
                        viewModel.getAnimationView().slideInLeft(binding.imageView)
                        viewModel.getAnimationView().slideInUp(binding.descriptionTextView)

                        val imageUrl = "${viewModel.getBaseImageUrl()}${anime?.categoryImage}"
                        binding.imageView.loadFromGlide(imageUrl)
                        showView()
                    }
                }
                is NetworkResources.Failure -> {
                    viewModel.getShowLoading().hideLoading()
                    hideView()
                }
            }
        }
    }

    private fun initLiveDataAnimeEp() {
        viewModel.animeEpResponseLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResources.Loading -> {}
                is NetworkResources.Succeeded -> {
                    viewModel.listEp = ArrayList(it.data)
                    viewModel.getAnimationView().slideInUp(binding.recyclerView)
                    binding.recyclerView.isGone = false
                    updateList()
                    binding.refreshView.isGone = true
                    showView()
                }
                is NetworkResources.Failure -> {
                    hideView()
                }
            }
        }
    }

    fun showView() {
        binding.apply {
            animeDetailContainer.isGone = false
            orderContainer.isGone = false
            editTextContainer.isGone = false
            recyclerView.isGone = false
            refreshView.isGone = true
        }
    }

    fun hideView() {
        binding.apply {
            animeDetailContainer.isGone = true
            orderContainer.isGone = true
            editTextContainer.isGone = true
            recyclerView.isGone = true
            refreshView.isGone = false
        }
    }

    private fun updateList() {
        val allWatchingEp =
            viewModel.getSharedPref().getWatchingEpByAnime(args.animeId ?: "")
        allWatchingEp.forEach { watchingEp ->
            val item =
                viewModel.listEp.firstOrNull { it.videoId == watchingEp.epId }
                    ?: return@forEach

            val index = viewModel.listEp.indexOf(item)

            if (index >= 0) {
                item.isWatchingEp = true
                viewModel.listEp[index] = item
            }
        }

        adapter?.replaceList(ArrayList(viewModel.listEp))
    }

    private fun saveFavoriteAnime() {
        val watched = WatchingEp(
            animeId = anime?.id,
            title = anime?.name,
            animeName = anime?.name,
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