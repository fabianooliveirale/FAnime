package com.example.home.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.home.databinding.FragmentCategoriesBinding
import com.example.screen_resources.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class CategoriesFragment : BaseFragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by sharedViewModel<CategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = CategoriesAdapter(viewModel.categoriesList) { categoryName ->
            viewModel.getRouter().goToAnimesCategory(binding.root, categoryName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}