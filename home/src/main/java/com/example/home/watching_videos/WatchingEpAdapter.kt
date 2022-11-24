package com.example.home.watching_videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dao.SharedPref
import com.example.home.databinding.AdapterAnimeEpItemBinding
import com.example.home.databinding.AdapterAnimeItemBinding
import com.example.home.databinding.AdapterAnimeItemVerticalBinding
import com.example.model.WatchingEp
import com.example.screen_resources.toMinutes

class WatchingEpAdapter(
    private val sharedPref: SharedPref,
    private val imageBaseUrl: String,
    private val itemClick: (WatchingEp) -> Unit = { }
) :
    RecyclerView.Adapter<WatchingEpAdapter.ViewHolder>() {

    private val dataSet: ArrayList<WatchingEp> = ArrayList()

    class ViewHolder(private val binding: AdapterAnimeItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            dataSet: WatchingEp,
            imageBaseUrl: String,
            itemClick: (WatchingEp) -> Unit
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

                val imageUrl = "${imageBaseUrl}${dataSet.image}"

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
        dataSet.addAll(sharedPref.getWatchingEp())
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

}
