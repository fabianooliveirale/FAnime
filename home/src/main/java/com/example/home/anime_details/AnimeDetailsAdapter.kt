package com.example.home.anime_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.model.*

class AnimeDetailsAdapter(
    private val itemClick: (AnimeEpResponse) -> Unit = {}
) : RecyclerView.Adapter<AnimeDetailsAdapter.ViewHolder>() {

    private var dataSet: ArrayList<AnimeEpResponse> = ArrayList()

    class ViewHolder(private val binding: AdapterAnimeEpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dataSet: AnimeEpResponse,
            itemClick: (AnimeEpResponse) -> Unit
        ) {
            binding.apply {
                titleTextView.text = dataSet.title

                mainView.background = if (dataSet.isWatchingEp) {
                    ContextCompat.getDrawable(
                        mainView.context,
                        com.example.screen_resources.R.drawable.background_rounded_watching
                    )
                } else {
                    ContextCompat.getDrawable(
                        mainView.context,
                        com.example.screen_resources.R.drawable.background_rounded
                    )
                }

                binding.iconView.isGone = !dataSet.isWatchingEp
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

    @SuppressLint("NotifyDataSetChanged")
    fun reversed() {
        dataSet = ArrayList(dataSet.reversed())
        notifyDataSetChanged()
    }

    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }
}
