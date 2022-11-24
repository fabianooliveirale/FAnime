package com.example.home.watching_videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.home.databinding.FragmentWatchingVideosBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WatchingVideosFragment : Fragment() {

    private var _binding: FragmentWatchingVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<WatchingViewModel>()
    private var adapter: WatchingEpAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchingVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSharedPref()
        adapter = WatchingEpAdapter(viewModel.getSharedPref(), viewModel.getImageUrl()) {
            viewModel.getRouter().goToVideo(
                this,
                it.epId ?: "",
                it.animeId ?: "",
                it.title ?: "",
                it.image ?: "",
                 it.position ?: 0
            )
        }
        binding.recyclerViewEp.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter?.refreshList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}