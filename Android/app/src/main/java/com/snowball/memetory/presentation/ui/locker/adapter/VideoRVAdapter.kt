package com.snowball.memetory.presentation.ui.locker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.databinding.ItemLockerVideoBinding

//class VideoRVAdapter(private val onItemClick: (String) -> Unit) :
//    RecyclerView.Adapter<VideoRVAdapter.ViewHolder>() {
//
//    var data = listOf<String>() // URL 리스트
//
//    fun updateData(newData: List<String>) {
//        data = newData
//        notifyDataSetChanged()  // 데이터가 변경되면 리사이클러뷰를 업데이트합니다.
//    }
//
//    class ViewHolder(private val binding: ItemLockerVideoBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(url: String, onClick: (String) -> Unit) {
//            binding.thumnailImg.setOnClickListener {
//                onClick(url)
//            }
//            // 이미지 로딩 로직 구현
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemLockerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val url = data[position]
//        holder.bind(url, onItemClick)
//    }
//
//    override fun getItemCount() = data.size
//}


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