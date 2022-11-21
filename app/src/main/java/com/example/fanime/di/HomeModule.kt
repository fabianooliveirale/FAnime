package com.example.fanime.di

import com.example.home.HomeViewModel
import com.example.home.api.HomeRepository
import com.example.home.api.HomeRepositoryImpl
import com.example.home.api.HomeService
import com.example.home.new_videos.NewVideosViewModel
import com.example.network.ApiFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    fun get(baseUrl: String, imageBaseUrl: String) = module {
        single {
            get<ApiFactory>().create(baseUrl, HomeService::class.java)
        }

        single<HomeRepository> {
            HomeRepositoryImpl(get(), get())
        }

        viewModel {
            HomeViewModel(get(), get(), get())
        }
        viewModel {
            NewVideosViewModel(get(), get(), get(), imageBaseUrl)
        }
    }
}