package com.example.home.watching_videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dao.SharedPref
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.home.databinding.AdapterAnimeItemVerticalBinding
import com.example.model.EpisodeModel
import com.example.model.WatchingEp
import com.example.screen_resources.toMinutes

class WatchingEpAdapter(
    private val sharedPref: SharedPref,
    private val imageBaseUrl: String,
    private val itemClick: (EpisodeModel) -> Unit = { }
) :
    RecyclerView.Adapter<WatchingEpAdapter.ViewHolder>() {

    private val dataSet: ArrayList<EpisodeModel> = ArrayList()

    class ViewHolder(private val binding: AdapterAnimeItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            dataSet: EpisodeModel,
            imageBaseUrl: String,
            itemClick: (EpisodeModel) -> Unit
        ) {
            binding.apply {
                titleTextView.text = dataSet.name
                epTextView.text = "Episódio: ${dataSet.number}"

                val imageUrl = "${imageBaseUrl}${dataSet.converImage}"

                timeTextView.text = dataSet.position?.toMinutes()

                Glide
                    .with(this.root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(com.example.screen_resources.R.drawable.progress_loading)
                    .into(binding.frontImageView)

                binding.mainView.setOnClickListener {
                    itemClick(dataSet)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterAnimeItemVerticalBinding.inflate(
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

    fun refreshList() {
        dataSet.clear()
        dataSet.addAll(sharedPref.getAllWatchedEpisode())
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

}
