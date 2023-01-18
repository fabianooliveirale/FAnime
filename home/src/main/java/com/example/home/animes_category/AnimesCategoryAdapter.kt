package com.example.home.animes_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.model.AnimesCategoryResponse
import com.example.screen_resources.extensions.loadFromGlide

class AnimesCategoryAdapter(
    private val dataSet: List<AnimesCategoryResponse>,
    private val imageBaseUrl: String,
    private val itemClick: (String) -> Unit = {}
) :
    RecyclerView.Adapter<AnimesCategoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterAnimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimesCategoryResponse,
            imageBaseUrl: String,
            itemClick: (String) -> Unit
        ) {
            binding.apply {
                titleTextView.text = dataSet.categoryName ?: ""
                titleTextView.maxLines = 3
                epTextView.isGone = true

                val imageUrl = "${imageBaseUrl}${dataSet.categoryImage}"

                binding.frontImageView.loadFromGlide(imageUrl)

                mainView.setOnClickListener {
                    itemClick(dataSet.id ?: "")
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterAnimeItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        viewHolder.bind(dataSet, imageBaseUrl, itemClick)
    }

    override fun getItemCount() = dataSet.size

}
