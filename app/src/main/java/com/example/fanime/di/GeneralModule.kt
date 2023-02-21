package com.example.fanime.di

import android.content.Context
import android.os.Handler
import com.example.dao.SharedPref
import com.example.fanime.MainViewModel
import com.example.network.NetworkBuilder
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.BaseViewModel
import com.example.screen_resources.Loop
import com.example.screen_resources.ShowLoading
import com.example.screen_resources.ViewAnimation
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object GeneralModule {
    fun get(baseUri: String, context: Context) = module {
        singleOf(::NetworkBuilder)
        singleOf(::NetworkScope)

        single {
            Gson()
        }

        single {
            ShowLoading()
        }

        single {
            Handler()
        }

        single {
            Loop()
        }

        single {
            Router(baseUri)
        }

        single {
            ViewAnimation(get())
        }

        single {
            SharedPref(context, get())
        }

        viewModel {
            MainViewModel(get())
        }

        viewModel {
            BaseViewModel(get())
        }
    }
}