package com.example.home.new_videos

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterNewVideosItemBinding
import com.example.home.model.NewVideosResponse
import com.squareup.picasso.Picasso

class NewVideosAdapter(
    private val dataSet: List<NewVideosResponse>,
    private val imageBaseUrl: String,
    private val itemClick: (String) -> Unit  = {}
) :
    RecyclerView.Adapter<NewVideosAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterNewVideosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataSet: NewVideosResponse, imageBaseUrl: String, itemClick: (String) -> Unit) {
            binding.apply {
                titleTextView.text = dataSet.title

                val imageUrl = "${imageBaseUrl}${dataSet.categoryImage}"
                Log.d("imageUrl_teste", imageUrl)

                Picasso.get()
                    .load(imageUrl)
                    .placeholder(com.example.screen_resources.R.drawable.progress_loading)
                    .into(binding.frontImageView)

                binding.mainView.setOnClickListener {
                    itemClick(dataSet.videoId ?: "")
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterNewVideosItemBinding.inflate(
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
