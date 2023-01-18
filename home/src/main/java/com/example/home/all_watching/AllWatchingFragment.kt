package com.example.home.all_watching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.example.home.anime_details.AnimeDetailsFragmentArgs
import com.example.home.databinding.FragmentAllWatchingBinding
import com.example.screen_resources.BaseFragment
import com.example.screen_resources.extensions.onTextChanged
import com.example.video.PlayerActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AllWatchingFragment : BaseFragment() {

    private var _binding: FragmentAllWatchingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<AllWatchingViewModel>()
    private val args: AllWatchingFragmentArgs by navArgs()

    private var adapter: AllWatchingAdapter? = null

    enum class WhereName {
        FROM_WATCHING, FROM_FAVORITE, FROM_ANIME
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllWatchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditTextWatcher()
        initAdapter()
        initToolbar()
    }

    private fun initToolbar() {
        val title = when (args.where) {
            WhereName.FROM_WATCHING.name -> "Episódios"
            WhereName.FROM_FAVORITE.name -> "Favoritos"
            else -> "Animes"
        }

        binding.toolbar.title = title
        binding.toolbar.setLeftIconClick {
            activity?.onBackPressed()
        }
    }

    private fun initAdapter() {
        val location = args.where
         viewModel.list = when (location) {
            WhereName.FROM_WATCHING.name -> viewModel.getSharedPref().getWatchingEp()
            WhereName.FROM_FAVORITE.name -> viewModel.getSharedPref().getFavoriteEp()
            else -> ArrayList(viewModel.getSharedPref().getWatchingEp().distinctBy { it.animeId })
        }

        adapter = AllWatchingAdapter(viewModel.getImageUrl(), location ==  WhereName.FROM_WATCHING.name) {
            if(location == WhereName.FROM_WATCHING.name) {
                viewModel.getRouter().goToVideo(
                    activity,
                    PlayerActivity(),
                    it.epId ?: "",
                    it.animeId ?: "",
                    it.title ?: "",
                    it.image ?: "",
                    it.position ?: 0
                )
            } else {
                viewModel.getRouter().goToAnimeDetails(
                    binding.root,
                    it.animeId ?: ""
                )
            }
        }

        binding.recyclerView.adapter = adapter

        adapter?.replaceList(viewModel.list)
    }

    private fun initEditTextWatcher() {
        viewModel.setSearchCallBack {
            adapter?.replaceList(it)
            binding.messageImageView.isGone = it.isNotEmpty()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}