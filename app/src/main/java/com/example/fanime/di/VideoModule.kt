package com.example.fanime.di

import com.example.network.ApiFactory
import com.example.video.VideoViewModel
import com.example.video.api.VideoRepository
import com.example.video.api.VideoRepositoryImpl
import com.example.video.api.VideoService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object VideoModule {
    fun get(baseUrl: String) = module {
        single {
            get<ApiFactory>().create(baseUrl, VideoService::class.java)
        }

        single<VideoRepository> {
            VideoRepositoryImpl(get(), get())
        }

        viewModel {
            VideoViewModel(get(), get(), get())
        }
    }
}