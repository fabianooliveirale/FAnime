package com.example.fanime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fanime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentItem = 0

    enum class FragmentId(val value: Int) {
        ANIME_DETAILS_ID(com.example.home.R.id.animeDetailsFragment),
        ANIME_CATEGORIES_ID(com.example.home.R.id.animesCategoryFragment);

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

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = currentItem
    }

    override fun onStop() {
        super.onStop()
        currentItem = binding.bottomNavigationView.selectedItemId
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