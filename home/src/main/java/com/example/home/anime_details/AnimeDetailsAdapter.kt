package com.example.home.anime_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.model.*

class AnimeDetailsAdapter(
    private val dataSet: List<AnimeEpResponse>,
    private val itemClick: (AnimeEpResponse) -> Unit = {}
) :
    RecyclerView.Adapter<AnimeDetailsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimeEpResponse,
            itemClick: (AnimeEpResponse) -> Unit
        ) {
            binding.apply {
                val splitTitle = dataSet.title?.split(" ")
                val special = if (splitTitle?.contains("Especial") == true) "Especial - " else ""

                val epNumber = dataSet.title?.split(" ")?.last()
                titleTextView.text = "${special}Episódio: $epNumber"

                mainView.setOnClickListener {
                    itemClick(dataSet)
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
