package com.example.video

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import com.example.network.NetworkResources
import com.example.video.databinding.FragmentVideoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<VideoViewModel>()
    val args: VideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startVideo()
        initLiveData()
    }

    private fun initLiveData() {
        viewModel.videoModelLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is NetworkResources.Loading -> {
                    val teste = "loading"
                }
                is NetworkResources.Succeeded -> {
                    binding.videoView.apply {
                        setVideoURI(it.data.first().location?.toUri());
                        requestFocus();
                        start()
                    }
                }
                is NetworkResources.Failure -> {
                    val teste = "error"
                }
            }
        }
    }

    private fun startVideo() {
        val videoID = args.videoId
        if(videoID == null) {
            Log.d("error", "valueNull")
            return
        }

        if(videoID == "") {
            Log.d("error", "valueNull")
            return
        }

        viewModel.getVideo(videoID)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}