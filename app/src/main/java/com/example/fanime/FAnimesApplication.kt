package com.example.fanime

import android.app.Application
import com.example.fanime.di.GeneralModule
import com.example.fanime.di.HomeModule
import com.example.fanime.di.NetworkModule
import com.example.fanime.di.VideoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FAnimesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@FAnimesApplication)

            modules(
                GeneralModule.get(BuildConfig.BASE_URI, this@FAnimesApplication),
                NetworkModule.get(),
                VideoModule.get(BuildConfig.BASE_URL),
                HomeModule.get(BuildConfig.BASE_URL, BuildConfig.BASE_URL_IMAGE)
            )
        }
    }

}