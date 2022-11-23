package com.example.video

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.navigation.navArgs
import com.example.network.NetworkResources
import com.example.video.databinding.ActivityPlayerBinding
import com.example.video.model.VideoModelResponse
import org.koin.android.ext.android.inject


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val args: PlayerActivityArgs by navArgs()
    private val viewModel: VideoViewModel by inject()
    private var mediaController: MediaController? = null
    private var videoId: String? = null
    private var animeId: String? = null
    private var previousData: VideoModelResponse? = null
    private var nextData: VideoModelResponse? = null
    private var requesting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = args.videoId
        animeId = args.animeId
        initLiveData()
        initLiveDataNext()
        initLiveDataPrevious()
        getVideoRequest()
        initMediaController()
        initMediaPlayerListener()
        initNextPrevious()
        refreshNextPreviou()
    }

    private fun refreshNextPreviou() {
        viewModel.previousEp(videoId ?: "", animeId ?: "")
        viewModel.nextEp(videoId ?: "", animeId ?: "")
    }

    private fun initNextPrevious() {
        binding.previousImageView.setOnClickListener {
            if (requesting) return@setOnClickListener
            videoStoped()
            setVideoTitle(previousData)
            videoId = previousData?.videoId
            startNewVideo(previousData?.locationSd ?: previousData?.location ?: "")
            refreshNextPreviou()
        }
        binding.nextImageView.setOnClickListener {
            if (requesting) return@setOnClickListener
            videoStoped()
            setVideoTitle(nextData)
            videoId = nextData?.videoId
            startNewVideo(nextData?.locationSd ?: nextData?.location ?: "")
            refreshNextPreviou()
        }
    }

    private fun startLoopPosition() {
        viewModel.getLoop().start(coroutineScope = viewModel) {
            if (binding.videoView.isPlaying)
                viewModel.currentPosition = binding.videoView.currentPosition
        }
    }

    private fun initMediaPlayerListener() {
        binding.videoView.setOnInfoListener { _, _, _ ->
            binding.loading.isGone = true
            startLoopPosition()
            true
        }
    }

    private fun initMediaController() {
        mediaController = object : MediaController(this) {
            override fun hide() {
                binding.previousImageView.isGone = true
                binding.nextImageView.isGone = true
                binding.videoTitle.isGone = true
                binding.shadowView.isGone = true
                super.hide()
            }

            override fun show() {
                binding.previousImageView.isGone = previousData == null
                binding.nextImageView.isGone = nextData == null
                binding.videoTitle.isGone = false
                binding.shadowView.isGone = false
                super.show()
            }
        }
        mediaController?.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }

    override fun onResume() {
        super.onResume()
        videoStoped()
        Log.d("VideoPlayerExample", "RESTART")
    }

    private fun videoStoped() {
        binding.videoView.pause()
        binding.loading.isGone = false
        viewModel.getLoop().stop()
    }

    private fun getVideoRequest() {
        if (videoId == null) {
            Log.d("error", "valueNull")
            return
        }

        if (videoId == "") {
            Log.d("error", "valueNull")
            return
        }

        viewModel.getVideo(videoId ?: "")
    }

    private fun initLiveData() {
        viewModel.videoModelLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {
                    requesting = true
                }
                is NetworkResources.Succeeded -> {
                    requesting = false
                    setVideoTitle(it.data.first())
                    startNewVideo(it.data.first().locationSd ?: it.data.first().location ?: "")
                }
                is NetworkResources.Failure -> {
                    requesting = false
                }
            }
        }
    }

    private fun setVideoTitle(dataSet: VideoModelResponse?) {
        val epNumber = dataSet?.title?.split(" ")?.last()
        binding.videoTitle.text = "Episódio: ${epNumber ?: ""}"
    }

    private fun initLiveDataNext() {
        viewModel.nextLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {
                    requesting = true
                }
                is NetworkResources.Succeeded -> {
                    requesting = false
                    nextData = it.data.first()
                }
                is NetworkResources.Failure -> {
                    requesting = false
                    nextData = null
                }
            }
        }
    }

    private fun initLiveDataPrevious() {
        viewModel.previousLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {
                    requesting = true
                }
                is NetworkResources.Succeeded -> {
                    requesting = false
                    previousData = it.data.first()
                }
                is NetworkResources.Failure -> {
                    requesting = false
                    previousData = null
                }
            }
        }
    }

    private fun startNewVideo(url: String) {
        binding.videoView.stopPlayback()
        viewModel.currentPosition = 0
        binding.videoView.setVideoURI(url.toUri())
        startVideoView()
    }

    override fun onStop() {
        super.onStop()
        binding.videoView.pause()
    }

    private fun startVideoView() {
        binding.videoView.let { videoView ->
            videoView.seekTo(viewModel.currentPosition)
            videoView.requestFocus()
            videoView.start()
        }
    }
}