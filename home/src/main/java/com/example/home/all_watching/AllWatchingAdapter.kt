package com.example.home.all_watching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterAnimeItemVerticalBinding
import com.example.home.databinding.AdapterAnimeItemVerticalGreatBinding
import com.example.model.WatchingEp
import com.example.screen_resources.extensions.loadFromGlide
import com.example.screen_resources.extensions.toMinutes

class AllWatchingAdapter(
    private val imageBaseUrl: String,
    private val showPosition: Boolean = false,
    private val itemClick: (WatchingEp) -> Unit = { }
) :
    RecyclerView.Adapter<AllWatchingAdapter.ViewHolder>() {

    private val dataSet: ArrayList<WatchingEp> = ArrayList()

    class ViewHolder(private val binding: AdapterAnimeItemVerticalGreatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            showPosition: Boolean,
            dataSet: WatchingEp,
            imageBaseUrl: String,
            itemClick: (WatchingEp) -> Unit
        ) {
            binding.apply {
                val splitTitle = dataSet.title?.split(" ")
                val count = splitTitle?.count()?.minus(1)
                var name = splitTitle?.mapIndexed { index, s ->
                    if (index != count && index != ((count ?: 0) - 1)) s else ""
                }?.joinToString(" ") ?: ""

                if(name.isBlank()) {
                    name = dataSet.title ?: ""
                }
                titleTextView.text = name

                val epNumber = dataSet.title?.split(" ")?.last()
                epTextView.text = "Episódio: $epNumber"
                epTextView.isGone = !showPosition

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
        val itemBinding = AdapterAnimeItemVerticalGreatBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val dataSet = dataSet[position]
        viewHolder.bind(showPosition, dataSet, imageBaseUrl, itemClick)
    }


    fun replaceList(watchingList: ArrayList<WatchingEp>) {
        dataSet.clear()
        dataSet.addAll(watchingList)
        notifyDataSetChanged()
    }

    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size

}
