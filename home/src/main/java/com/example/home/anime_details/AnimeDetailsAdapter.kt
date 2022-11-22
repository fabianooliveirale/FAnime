package com.example.home.anime_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.home.model.AnimeEpResponse
import com.example.home.model.AnimesCategoryResponse

class AnimeDetailsAdapter(
    private val dataSet: List<AnimeEpResponse>,
    private val itemClick: (String) -> Unit = {}
) :
    RecyclerView.Adapter<AnimeDetailsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimeEpResponse,
            itemClick: (String) -> Unit
        ) {
            binding.apply {
                val splitTitle = dataSet.title?.split(" ")
                val count = splitTitle?.count()?.minus(1)
                val name = splitTitle?.mapIndexed { index, s ->
                    if (index != count && index != ((count ?: 0) - 1)) s else ""
                }?.joinToString(" ") ?: ""

                val special = if (splitTitle?.contains("Especial") == true) "Especial - " else ""

                val epNumber = dataSet.title?.split(" ")?.last()
                titleTextView.text = "${special}Episódio: $epNumber"

                mainView.setOnClickListener {
                    itemClick(dataSet.videoId ?: "")
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterAnimeEpItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        viewHolder.bind(dataSet, itemClick)
    }

    override fun getItemCount() = dataSet.size

}
