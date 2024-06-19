package com.snowball.memetory.presentation.ui.locker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.databinding.ItemLockerVideoBinding

class VideoRVAdapter(private val onVideoClick: (String) -> Unit) :
    ListAdapter<String, VideoRVAdapter.VideoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemLockerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUrl = getItem(position)
        holder.bind(videoUrl)
    }

    inner class VideoViewHolder(private val binding: ItemLockerVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoUrl: String) {
            binding.thumnailImg.setOnClickListener { onVideoClick(videoUrl) }
            // Set thumbnail image using Glide or another library
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}