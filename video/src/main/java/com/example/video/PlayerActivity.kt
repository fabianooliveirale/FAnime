package com.example.video

import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.navigation.navArgs
import com.example.network.NetworkResources
import com.example.video.databinding.ActivityPlayerBinding
import org.koin.android.ext.android.inject


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val args: PlayerActivityArgs by navArgs()
    private val viewModel: VideoViewModel by inject()
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLiveData()
        getVideoRequest()
        initMediaController()
        initMediaPlayerListener()
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
        mediaController = MediaController(this)
        mediaController?.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }

    override fun onResume() {
        super.onResume()
        Log.d("VideoPlayerExample", "ONRESUME")
        binding.loading.isGone = false
        viewModel.getLoop().stop()
        if (viewModel.currentPosition > 0 && viewModel.uri != null) {
            Log.d("VideoPlayerExample", "RESTART")
            startVideoView()
        } else {

            Log.d(
                "VideoPlayerExample",
                "notRestart \n position: ${viewModel.currentPosition} \n uri"
            )
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

    private fun initLiveData() {
        viewModel.videoModelLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {}
                is NetworkResources.Succeeded -> {
                    Log.d("VideoPlayerExample", "CHAMADA")
                    viewModel.uri = it.data.first().location?.toUri()
                    binding.videoView.setVideoURI(viewModel.uri)
                    startVideoView()
                }
                is NetworkResources.Failure -> {
                    val teste = "error"
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.videoView.stopPlayback()
    }

    private fun startVideoView() {
        binding.videoView.let { videoView ->
            videoView.seekTo(viewModel.currentPosition)
            videoView.requestFocus()
            videoView.start()
            Log.d("VideoPlayerExample", "PLAY: position ${viewModel.currentPosition}")
        }
    }
}