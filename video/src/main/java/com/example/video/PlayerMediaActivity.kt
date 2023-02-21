package com.example.video

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener
import com.devbrackets.android.exomedia.ui.listener.VideoControlsButtonListener
import com.devbrackets.android.exomedia.ui.listener.VideoControlsSeekListener
import com.devbrackets.android.exomedia.ui.listener.VideoControlsVisibilityListener
import com.devbrackets.android.exomedia.ui.widget.controls.DefaultVideoControls
import com.devbrackets.android.exomedia.ui.widget.controls.VideoControlsMobile
import com.example.model.AnimeDetailsResponse
import com.example.model.WatchingEp
import com.example.network.NetworkResources
import com.example.video.databinding.ActivityPlayerMediaBinding
import com.example.video.model.VideoModelResponse
import org.koin.android.ext.android.inject
import java.util.*

class PlayerMediaActivity : AppCompatActivity() {
    companion object {
        const val TEN_SEC = 10000
    }

    private lateinit var binding: ActivityPlayerMediaBinding
    private val viewModel: VideoViewModel by inject()
    private var videoId: String? = null
    private var animeId: String? = null
    private var title: String? = null
    private var imageUrl: String? = null
    private var previousData: VideoModelResponse? = null
    private var nextData: VideoModelResponse? = null
    private var requesting = false
    private var currentPosition = 0
    private var url: String? = null
    private var anime: AnimeDetailsResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = intent.getStringExtra("videoId").toString()
        animeId = intent.getStringExtra("animeId").toString()
        title = intent.getStringExtra("title").toString()
        imageUrl = intent.getStringExtra("imageUrl").toString()
        currentPosition = intent.getIntExtra("currentPosition", 0)
        initLiveData()
        initLiveDataNext()
        initLiveDataPrevious()
        initLiveDataAnimeDetails()
        getVideoRequest()
        refreshNextPreviou()
        viewModel.getAnimeDetails(animeId ?: "")

        (binding.videoView.videoControls as? DefaultVideoControls)?.apply {
            buttonsListener = OnButtonControl()
        }
        (binding.videoView.videoControls as? VideoControlsMobile)?.apply {
            seekCallBack = {
                currentPosition = it
            }
        }
    }

    private fun refreshNextPreviou() {
        viewModel.previousEp(videoId ?: "", animeId ?: "")
        viewModel.nextEp(videoId ?: "", animeId ?: "")
    }

    override fun onResume() {
        super.onResume()
        if (!binding.videoView.isPlaying) {
            saveWatchingVideo()
        }
    }

    private fun stopVideo() {
        if (binding.videoView.isPlaying) {
            saveWatchingVideo()
            binding.videoView.pause()
        }
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

                    binding.refreshView.isGone = true
                    startNewVideo(url ?: "")
                }
                is NetworkResources.Failure -> {
                    requesting = false
                    binding.refreshView.isGone = false
                }
            }
        }
    }

    private fun setVideoTitle(dataSet: VideoModelResponse?) {
       (binding.videoView.videoControls as VideoControlsMobile).setTitle(dataSet?.title ?: "")
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
                    (binding.videoView.videoControls as VideoControlsMobile).setNextButtonRemoved(false)
                }
                is NetworkResources.Failure -> {
                    requesting = false
                    nextData = null
                }
            }
        }
    }

    private fun initLiveDataAnimeDetails() {
        viewModel.animeDetailsLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {
                }
                is NetworkResources.Succeeded -> {
                    anime = it.data.first()
                }
                is NetworkResources.Failure -> {
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
                    (binding.videoView.videoControls as VideoControlsMobile).setPreviousButtonRemoved(false)
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
        startVideo()
        resumeVideoView()
    }

    override fun onStop() {
        super.onStop()
        (binding.videoView.videoControls as? DefaultVideoControls)?.setButtonListener(null)
        stopVideo()
    }

    private fun resumeVideoView() {
        binding.videoView.let { videoView ->
            videoView.seekTo(currentPosition.toLong())
            videoView.requestFocus()
            videoView.start()
        }
    }

    private fun saveWatchingVideo() {
        if (url == null) return
        if (url == "") return
        val watched = WatchingEp(
            animeName = anime?.name,
            epId = videoId,
            animeId = animeId,
            title = title,
            position = currentPosition,
            image = imageUrl,
            time = Date()
        )

        if (watched.title == null || watched.animeName == null) return
        viewModel.getSharedPref().saveWatchingEp(watched)
    }

    override fun onDestroy() {
        saveWatchingVideo()
        (binding.videoView.videoControls as? DefaultVideoControls)?.setButtonListener(null)
        super.onDestroy()
    }

    private fun startVideo() {
        if(url == null) return
        binding.videoView.apply {
            setMedia(Uri.parse(url))
            start()
        }
    }

    private inner class OnButtonControl: VideoControlsButtonListener {
        override fun onPlayPauseClicked(): Boolean {
            return binding.videoView?.let {
                if (it.isPlaying) {
                    it.pause()
                } else {
                    it.start()
                }
                true
            } ?: false
        }

        override fun onPreviousClicked(): Boolean {
            viewModel.getRouter().goToVideo(
                this@PlayerMediaActivity,
                PlayerMediaActivity(),
                previousData?.videoId ?: "",
                animeId ?: "",
                previousData?.title ?: "",
                imageUrl ?: "",
                0
            )
            finish()
            return true
        }

        override fun onNextClicked(): Boolean {
            viewModel.getRouter().goToVideo(
                this@PlayerMediaActivity,
                PlayerMediaActivity(),
                nextData?.videoId ?: "",
                animeId ?: "",
                nextData?.title ?: "",
                imageUrl ?: "",
                0
            )
            finish()
            return true
        }

        override fun onRewindClicked(): Boolean {

            return true
        }

        override fun onFastForwardClicked(): Boolean {
            return true
        }

        override fun onBackArrowClicked(): Boolean {
            saveWatchingVideo()
            finish()
            return true
        }

        override fun onLeftAreaDoubleClick(): Boolean {
            currentPosition -= TEN_SEC
            resumeVideoView()
            return true
        }

        override fun onRightAreaDoubleClick(): Boolean {
            currentPosition += TEN_SEC
            resumeVideoView()
            return true
        }
    }
}