package com.example.home.anime_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.SharedPref
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.model.EpisodeModel

class AnimeDetailsAdapter(
    private val list: ArrayList<EpisodeModel>,
    private val itemClick: (EpisodeModel) -> Unit = {}
) :
    RecyclerView.Adapter<AnimeDetailsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: EpisodeModel,
            itemClick: (EpisodeModel) -> Unit
        ) {
            binding.apply {
                val splitTitle = dataSet.fullName?.split(" ")
                val special = if (splitTitle?.contains("Especial") == true) "Especial - " else ""

                val epNumber = dataSet.fullName?.split(" ")?.last()
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
        val dataSet = list[position]
        viewHolder.bind(dataSet, itemClick)
    }

    override fun getItemCount() = list.size

}
