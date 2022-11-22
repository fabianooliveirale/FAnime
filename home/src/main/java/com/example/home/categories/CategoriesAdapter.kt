package com.example.home.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.databinding.AdapterCategoriesItemBinding
import com.example.screen_resources.capitalized

class CategoriesAdapter(
    private val dataSet: List<String>,
    private val itemClick: (String) -> Unit = {}
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterCategoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataSet: String, itemClick: (String) -> Unit) {
            binding.apply {
                titleTextView.text = dataSet.capitalized()
                mainView.setOnClickListener {
                    itemClick(dataSet)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AdapterCategoriesItemBinding.inflate(
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
