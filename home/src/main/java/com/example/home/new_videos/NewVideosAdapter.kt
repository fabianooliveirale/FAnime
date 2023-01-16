package com.example.home.new_videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.model.NewVideosResponse
import com.example.screen_resources.extensions.loadFromGlide

class NewVideosAdapter(
    private val dataSet: List<NewVideosResponse>,
    private val imageBaseUrl: String,
    private val itemClick: (NewVideosResponse) -> Unit = { }
) :
    RecyclerView.Adapter<NewVideosAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterAnimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            dataSet: NewVideosResponse,
            imageBaseUrl: String,
            itemClick: (NewVideosResponse) -> Unit
        ) {
            binding.apply {
                val splitTitle = dataSet.title?.split(" ")
                val count = splitTitle?.count()?.minus(1)
                val name = splitTitle?.mapIndexed { index, s ->
                    if (index != count && index != ((count ?: 0) - 1)) s else ""
                }?.joinToString(" ") ?: ""
                titleTextView.text = name

                val epNumber = dataSet.title?.split(" ")?.last()
                epTextView.text = "Episódio: $epNumber"

                val imageUrl = "${imageBaseUrl}${dataSet.categoryImage}"

                binding.frontImageView.loadFromGlide(imageUrl)

                binding.mainView.setOnClickListener {
                    itemClick(dataSet)
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

}
