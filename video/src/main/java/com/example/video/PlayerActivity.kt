package com.example.video

import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.example.model.WatchingEp
import com.example.network.NetworkResources
import com.example.screen_resources.extensions.getLastItemNumber
import com.example.video.databinding.ActivityPlayerBinding
import com.example.video.model.VideoModelResponse
import org.koin.android.ext.android.inject
import java.util.*


class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TEN_SEC = 10000
    }

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: VideoViewModel by inject()
    private var mediaController: MediaController? = null
    private var videoId: String? = null
    private var animeId: String? = null
    private var title: String? = null
    private var imageUrl: String? = null
    private var previousData: VideoModelResponse? = null
    private var nextData: VideoModelResponse? = null
    private var requesting = false
    private var currentPosition = 0
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = intent.getStringExtra("videoId").toString()
        animeId = intent.getStringExtra("animeId").toString()
        title = intent.getStringExtra("title").toString()
        imageUrl = intent.getStringExtra("imageUrl").toString()
        currentPosition = intent.getIntExtra("currentPosition", 0)
        initLiveData()
        initLiveDataNext()
        initLiveDataPrevious()
        getVideoRequest()
        initMediaController()
        initMediaPlayerListener()
        initNextPrevious()
        refreshNextPreviou()
        initButton()
    }

    private fun initButton() {
        binding.backPressView.setOnClickListener {
            saveWatchingVideo()
            onBackPressed()
        }

        binding.forward10ImageView.setOnClickListener {
            Log.d("teste_teste_teste", "$currentPosition = ${currentPosition + 10}")
            currentPosition += TEN_SEC
            resumeVideoView()
        }

        binding.replay10ImageView.setOnClickListener {
            Log.d("teste_teste_teste", "$currentPosition = ${currentPosition - 10}")
            currentPosition -= TEN_SEC
            resumeVideoView()
        }

        binding.returnToStartView.setOnClickListener {
            currentPosition = 0
            resumeVideoView()
        }
    }

    private fun refreshNextPreviou() {
        viewModel.previousEp(videoId ?: "", animeId ?: "")
        viewModel.nextEp(videoId ?: "", animeId ?: "")
    }

    private fun initNextPrevious() {
        binding.previousImageView.setOnClickListener {
            if (requesting) return@setOnClickListener
            stopVideo()
            currentPosition = 0
            setVideoTitle(previousData)
            videoId = previousData?.videoId
            title = previousData?.title
            val url = if (nextData?.locationSd?.isNotEmpty() == true) {
                nextData?.locationSd
            } else {
                nextData?.location
            }
            startNewVideo(url ?: "")
            refreshNextPreviou()
            saveWatchingVideo()
        }
        binding.nextImageView.setOnClickListener {
            if (requesting) return@setOnClickListener
            stopVideo()
            currentPosition = 0
            setVideoTitle(nextData)
            videoId = nextData?.videoId
            title = previousData?.title

            val url = if (nextData?.locationSd?.isNotEmpty() == true) {
                nextData?.locationSd
            } else {
                nextData?.location
            }

            startNewVideo(url ?: "")
            refreshNextPreviou()
            saveWatchingVideo()
        }
    }

    private fun startLoopPosition() {
        viewModel.getLoop().start(coroutineScope = viewModel) {
            if (binding.videoView.isPlaying) {
                currentPosition = binding.videoView.currentPosition
            }
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
                binding.backPressView.isGone = true
                binding.returnToStartView.isGone = true
                binding.forward10ImageView.isGone = true
                binding.replay10ImageView.isGone = true
                super.hide()
            }

            override fun show() {
                binding.previousImageView.isGone = previousData == null
                binding.nextImageView.isGone = nextData == null
                binding.videoTitle.isGone = false
                binding.shadowView.isGone = false
                binding.backPressView.isGone = false
                binding.returnToStartView.isGone = false
                binding.forward10ImageView.isGone = false
                binding.replay10ImageView.isGone = false
                super.show()
            }
        }
        mediaController?.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }

    override fun onResume() {
        super.onResume()
        saveWatchingVideo()
        binding.loading.isGone = false
    }

    private fun stopVideo() {
        saveWatchingVideo()
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

                    url = if (it.data.first().locationSd != "") {
                        it.data.first().locationSd
                    } else {
                        it.data.first().location
                    }

                    startNewVideo(url ?: "")
                }
                is NetworkResources.Failure -> {
                    requesting = false
                }
            }
        }
    }

    private fun setVideoTitle(dataSet: VideoModelResponse?) {
        val epCount = dataSet?.title?.split(" ")?.count() ?: -1
        val special =
            if (dataSet?.title?.split(" ")?.contains("Especial") == true) "Especial - " else ""
        if (epCount >= 0) {
            val epNumber = dataSet?.title?.split(" ")?.getLastItemNumber(epCount - 1)
                ?.replaceFirst("^0*".toRegex(), "")

            binding.videoTitle.text = "${special}Episódio: $epNumber"
        }
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
        if (url == "") finish()
        binding.videoView.stopPlayback()
        binding.videoView.setVideoURI(url.toUri())

        resumeVideoView()
    }

    override fun onStop() {
        super.onStop()
        stopVideo()
    }

    private fun resumeVideoView() {
        binding.videoView.let { videoView ->
            videoView.seekTo(currentPosition)
            videoView.requestFocus()
            videoView.start()
        }
    }

    private fun saveWatchingVideo() {
        if (url == null) return
        if (url == "") return
        val watched = WatchingEp(
            epId = videoId,
            animeId = animeId,
            title = title,
            position = currentPosition,
            image = imageUrl,
            time = Date()
        )
        viewModel.getSharedPref().saveWatchingEp(watched)
    }

    override fun onBackPressed() {
        saveWatchingVideo()
        super.onBackPressed()
    }
}