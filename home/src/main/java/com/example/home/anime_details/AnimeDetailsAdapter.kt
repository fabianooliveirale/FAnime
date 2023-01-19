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

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimeEpResponse,
            itemClick: (AnimeEpResponse) -> Unit
        ) {
            binding.apply {
                titleTextView.text = dataSet.title

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

    @SuppressLint("NotifyDataSetChanged")
    fun replaceList(watchingList: ArrayList<AnimeEpResponse>) {
        dataSet.clear()
        dataSet.addAll(watchingList)
        notifyDataSetChanged()
    }
}
