package com.example.home.new_videos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.FragmentNewVideosBinding
import com.example.home.watching_videos.WatchingViewModel
import com.example.network.NetworkResources
import com.example.screen_resources.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewVideosFragment : BaseFragment() {

    private var _binding: FragmentNewVideosBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<NewVideosViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLiveData()
        if (viewModel.newVideosData == null)
            viewModel.getNewVideos()
    }

    private fun initLiveData() {
        viewModel.newVideosLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    viewModel.getShowLoading().showLoading()
                }
                is NetworkResources.Succeeded -> {
                    viewModel.getShowLoading().hideLoading()
                    viewModel.newVideosData = it.data
                    binding.recyclerView.adapter =
                        NewVideosAdapter(
                            it.data,
                            viewModel.getBaseImageUrl()
                        ) { item ->
                            viewModel.getRouter().goToAnimeDetails(
                                binding.root,
                                item.categoryId ?: ""
                            )
                        }
                }
                is NetworkResources.Failure -> {
                    viewModel.getShowLoading().hideLoading()
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