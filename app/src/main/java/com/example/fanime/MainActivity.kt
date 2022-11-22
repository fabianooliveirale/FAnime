package com.example.fanime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fanime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    enum class FragmentId(val value: Int) {
        VIDEO_FRAGMENT_ID(com.example.video.R.id.videoFragment),
        ANIME_DETAILS_ID(com.example.home.R.id.animeDetailsFragment);

        companion object {
            fun fromInt(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        binding.apply {
            navHostFragment?.findNavController()?.let { navController ->
                bottomNavigationView.setupWithNavController(navController)

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    binding.bottomNavigationView.isGone =
                        checkIsFragmentNotContainsBottomNavigation(destination.id)
                }
            }
        }
    }

    private fun checkIsFragmentNotContainsBottomNavigation(id: Int): Boolean {
        return FragmentId.fromInt(id) != null
    }
}