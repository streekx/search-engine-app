package com.ansh.searchengine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ansh.searchengine.databinding.ItemResultBinding

class ResultAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var list = listOf<SearchResult>()

    fun submitList(newList: List<SearchResult>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResult) {
            binding.titleText.text = item.title
            binding.urlText.text = item.url
            binding.root.setOnClickListener {
                onClick(item.url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
    }
