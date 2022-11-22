package com.example.home.watched_videos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.FragmentWatchedVideosBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WatchedVideosFragment : Fragment() {

    private var _binding: FragmentWatchedVideosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchedVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}