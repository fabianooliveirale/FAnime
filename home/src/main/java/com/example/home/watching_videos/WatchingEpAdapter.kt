package com.example.home.watching_videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.dao.SharedPref
import com.example.home.databinding.AdapterAnimeItemVerticalBinding
import com.example.model.WatchingEp
import com.example.screen_resources.extensions.loadFromGlide
import com.example.screen_resources.extensions.toMinutes

class WatchingEpAdapter(
    private val type: Type,
    private val imageBaseUrl: String,
    private val showPosition: Boolean = false,
    private val itemClick: (WatchingEp) -> Unit = { }
) :
    RecyclerView.Adapter<WatchingEpAdapter.ViewHolder>() {

    private val dataSet: ArrayList<WatchingEp> = ArrayList()

    enum class Type {
        EP_NAME, ANIME_NAME
    }

    class ViewHolder(private val binding: AdapterAnimeItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            type: Type,
            showPosition: Boolean,
            dataSet: WatchingEp,
            imageBaseUrl: String,
            itemClick: (WatchingEp) -> Unit
        ) {
            binding.apply {
                titleTextView.text = if(type == Type.EP_NAME) {
                    dataSet.title
                } else {
                    dataSet.animeName
                }

                val imageUrl = "${imageBaseUrl}${dataSet.image}"

                timeTextView.text = dataSet.position?.toMinutes()
                timeTextView.isGone = !showPosition

                binding.frontImageView.loadFromGlide(imageUrl)

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
        viewHolder.bind(type, showPosition, dataSet, imageBaseUrl, itemClick)
    }


    fun replaceList(watchingList: ArrayList<WatchingEp>) {
        dataSet.clear()
        dataSet.addAll(watchingList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

}
