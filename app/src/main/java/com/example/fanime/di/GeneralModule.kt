package com.example.fanime.di

import android.os.Handler
import com.example.network.NetworkBuilder
import com.example.network.NetworkScope
import com.example.router.Router
import com.example.screen_resources.Loop
import com.example.screen_resources.ViewAnimation
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object GeneralModule {
    fun get(baseUri: String) = module {
        singleOf(::NetworkBuilder)
        singleOf(::NetworkScope)

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
    }
}