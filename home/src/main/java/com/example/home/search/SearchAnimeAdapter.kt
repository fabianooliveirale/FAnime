package com.example.home.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.home.model.NewVideosResponse
import com.example.home.model.SearchResponse
import com.example.screen_resources.ViewAnimation

class SearchAnimeAdapter(
    private val imageBaseUrl: String,
    private val itemClick: (String) -> Unit = {}
) :
    RecyclerView.Adapter<SearchAnimeAdapter.ViewHolder>() {

    private val dataSet: ArrayList<SearchResponse> = ArrayList()

    class ViewHolder(private val binding: AdapterAnimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            dataSet: SearchResponse,
            imageBaseUrl: String,
            itemClick: (String) -> Unit
        ) {
            binding.apply {
                titleTextView.text = dataSet.categoryName
                epTextView.isGone = true

                val imageUrl = "${imageBaseUrl}${dataSet.categoryImage}"

                Glide
                    .with(this.root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(com.example.screen_resources.R.drawable.progress_loading)
                    .into(binding.frontImageView)

                binding.mainView.setOnClickListener {
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
        viewHolder.bind(position, dataSet, imageBaseUrl, itemClick)
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun replace(dataSet: ArrayList<SearchResponse>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        this.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.dataSet.clear()
        this.notifyDataSetChanged()
    }
}
