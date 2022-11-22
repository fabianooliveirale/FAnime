package com.example.video

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout.LayoutParams
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.network.NetworkResources
import com.example.video.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by sharedViewModel<VideoViewModel>()
    val args: VideoFragmentArgs by navArgs()

    private var defaultVideoViewParams: LayoutParams? = null
    private var defaultScreenOrientationMode = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMediaController()
        initLiveData()
        getVideoRequest()
        setFullScreen()
    }

    private fun setFullScreen() {

//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            activity?.window?.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
    }

    private fun initMediaController() {

    }

    private fun initLiveData() {
        viewModel.videoModelLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResources.Loading -> {
                    val teste = "loading"
                }
                is NetworkResources.Succeeded -> {
                    val uri = it.data.first().location?.toUri()
                    uri?.let { value ->
                        startVideoView(value)
                    }
                }
                is NetworkResources.Failure -> {
                    val teste = "error"
                }
            }
        }
    }

    private fun startVideoView(uri: Uri) {
        val mediItem = MediaItem.fromUri(uri)
        val player = ExoPlayer.Builder(this.requireContext()).build()
        player.setMediaItem(mediItem)
        player.play()
        player.prepare()
        binding.videoView.apply {
            this.player = player
        }
    }

    private fun getVideoRequest() {
        val videoID = args.videoId
        if (videoID == null) {
            Log.d("error", "valueNull")
            return
        }

        if (videoID == "") {
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