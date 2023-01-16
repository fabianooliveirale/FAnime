package com.example.home.anime_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.model.*

class AnimeDetailsAdapter(
    private val itemClick: (AnimeEpResponse) -> Unit = {}
) : RecyclerView.Adapter<AnimeDetailsAdapter.ViewHolder>() {

    private val dataSet: ArrayList<AnimeEpResponse> = ArrayList()
    private var showShimmer = true

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimeEpResponse,
            itemClick: (AnimeEpResponse) -> Unit,
            showShimmer: Boolean = true
        ) {
            binding.apply {
                titleTextView.text = dataSet.epNumberName

                mainView.setOnClickListener {
                    itemClick(dataSet)
                }

                if(!showShimmer) shimmerViewContainer.hideShimmer()
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
        viewHolder.bind(dataSet, itemClick, showShimmer)
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun replaceList(watchingList: ArrayList<AnimeEpResponse>) {
        dataSet.clear()
        dataSet.addAll(watchingList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun hideShimmer() {
        dataSet.clear()
        showShimmer = false
        notifyDataSetChanged()
    }
}
