package com.example.nitroce

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nitroce.databinding.ItemAlbumBinding
import com.example.nitroce.utils.FileItem

class ItemAdapter(private var item: MutableList<FileItem>): RecyclerView.Adapter<ItemAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size

    interface OnItemClickCallback {
        fun onItemClicked(item: FileItem)
    }

    inner class ListViewHolder(var binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FileItem){
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(item)
            }
            binding.apply{
                Glide.with(itemView)
                    .load(item.path)
                    .into(imgItem)
                tvName.text = item.name
            }
        }
    }
}