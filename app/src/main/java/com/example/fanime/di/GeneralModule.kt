package com.example.fanime.di

import com.example.network.NetworkBuilder
import com.example.network.NetworkScope
import com.example.router.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object GeneralModule {
    fun get(baseUri: String) = module {
        singleOf(::NetworkBuilder)
        singleOf(::NetworkScope)
        single {
            Router(baseUri)
        }
    }
}