package com.example.fanime.di

import com.example.home.HomeViewModel
import com.example.home.all_watching.AllWatchingViewModel
import com.example.home.anime_details.AnimeDetailsViewModel
import com.example.home.animes_category.AnimesCategoryViewModel
import com.example.home.api.HomeRepository
import com.example.home.api.HomeRepositoryImpl
import com.example.home.api.HomeService
import com.example.home.categories.CategoriesViewModel
import com.example.home.new_videos.NewVideosViewModel
import com.example.home.search.SearchAnimeViewModel
import com.example.home.watching_videos.WatchingViewModel
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
            CategoriesViewModel(get(), get())
        }

        viewModel {
            AnimesCategoryViewModel(get(), get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            NewVideosViewModel(get(), get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            AnimeDetailsViewModel(get(), get(), get(), get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            SearchAnimeViewModel(get(), get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            WatchingViewModel(get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            AllWatchingViewModel(get(), get(), get(), imageBaseUrl)
        }

        viewModel {
            HomeViewModel()
        }
    }
}