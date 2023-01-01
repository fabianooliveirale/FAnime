package com.example.video

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.navArgs
import com.example.model.EpisodeModel
import com.example.model.VideoModelResponse
import com.example.network.NetworkResources
import com.example.video.databinding.ActivityPlayerBinding
import org.koin.android.ext.android.inject
import java.util.*


class PlayerActivity : AppCompatActivity(), PlayerInterface {

    private lateinit var binding: ActivityPlayerBinding
    private val args: PlayerActivityArgs by navArgs()
    private val viewModel: VideoViewModel by inject()
    private var mediaController: MediaController? = null
    private var previousData: VideoModelResponse? = null
    private var nextData: VideoModelResponse? = null
    private var requesting = false
    private var episode: EpisodeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refreshEpisode()
        initLiveData()
        initLiveDataNext()
        initLiveDataPrevious()
        initMediaController()
        initMediaPlayerListener()
        initNextPreviousButton()
        requestNextAndPrevisouData()
        viewModel.getVideo(episode?.id ?: "")
    }

    private fun requestNextAndPrevisouData() {
        viewModel.previousEp(episode?.id ?: "", episode?.animeId ?: "")
        viewModel.nextEp(episode?.id ?: "", episode?.animeId ?: "")
    }

    private fun initNextPreviousButton() {
        binding.previousImageView.setOnClickListener {
            this.previous()
        }
        binding.nextImageView.setOnClickListener {
            this.next()
        }
    }

    private fun initMediaPlayerListener() {
        binding.videoView.setOnInfoListener { _, _, _ ->
            binding.loading.isGone = true
            updatePositon()
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
                binding.playImageView.isGone = true
                super.hide()
            }

            override fun show() {
                binding.previousImageView.isGone = previousData == null
                binding.nextImageView.isGone = nextData == null
                binding.videoTitle.isGone = false
                binding.shadowView.isGone = false
                binding.playImageView.isGone = true
                super.show()
            }
        }
        mediaController?.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }

    override fun onResume() {
        super.onResume()
        save()
        binding.loading.isGone = false
    }

    private fun initLiveData() {
        viewModel.videoModelLiveData.observe(this) {
            when (it) {
                is NetworkResources.Loading -> {
                    requesting = true
                }
                is NetworkResources.Succeeded -> {
                    requesting = false
                    refreshEpisode()
                    play()
                }
                is NetworkResources.Failure -> {
                    requesting = false
                }
            }
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

    override fun onStop() {
        this.pause()
        super.onStop()
    }

    override fun onBackPressed() {
        this.pause()
        this.finish()
    }

    override fun pause() {
        binding.videoView.pause()
        binding.loading.isGone = false
        viewModel.getLoop().stop()
        updatePositon()
        save()
    }

    override fun play() {
        val uri = episode?.getVideoUri()
        binding.videoView.stopPlayback()
        binding.videoView.setVideoURI(uri)
        updateLayoutTitle()

        save()
        resume()
    }


    override fun resume() {
        binding.videoView.let { videoView ->
            episode?.let { episode ->
                videoView.seekTo(episode.position ?: 0)
                videoView.requestFocus()
                videoView.start()
            }
        }
    }

    override fun save() {
        if (episode?.getVideoUri() == null) return
        episode?.let {
            updateIsWatch()
            updateTime()
            updateEpisodeCoverImage()
            viewModel.getSharedPref().saveEpisode(it)
        }
    }

    override fun updatePositon() {
        viewModel.getLoop().start(coroutineScope = viewModel) {
            if (binding.videoView.isPlaying) {
                episode?.position = binding.videoView.currentPosition
            }
        }
    }

    override fun updateTime() {
        episode?.watchingTime = Date()
    }

    override fun updateIsWatch() {
        episode?.watched = true
    }

    override fun updateLayoutTitle() {
        binding.videoTitle.text = "Episódio: ${episode?.number ?: 0}"
    }

    override fun updateEpisodeCoverImage() {
        episode?.converImage = viewModel.getAnime(args.animeId ?: "")?.coverImage
    }

    override fun updateEpisode(animeId: String, episodeId: String) {
        this.episode = viewModel.getEpisode(animeId, episodeId)
        play()
    }

    override fun next() {
        if (requesting) return
        save()
        updateEpisode(previousData?.animeId ?: "", previousData?.videoId ?: "")
        requestNextAndPrevisouData()
    }

    override fun previous() {
        if (requesting) return
        updateEpisode(nextData?.animeId ?: "", nextData?.videoId ?: "")
        requestNextAndPrevisouData()
    }

    override fun refreshEpisode() {
        episode = viewModel.getEpisode(args.animeId ?: "", args.episodeId ?: "")
    }
}