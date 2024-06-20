package com.snowball.memetory.presentation.ui.locker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.databinding.ItemLockerVideoBinding
import com.snowball.memetory.domain.model.locker.Video
import com.snowball.memetory.presentation.ui.locker.VideoOptionsBottomSheetFragment


class VideoRVAdapter(
    private val fragmentManager: FragmentManager,
    private val onVideoClick: (String) -> Unit
) : ListAdapter<Video, VideoRVAdapter.VideoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemLockerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VideoViewHolder(private val binding: ItemLockerVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoData: Video) {
            binding.thumnailImg.setOnClickListener { onVideoClick(videoData.s3Url) }
            binding.settingImg.setOnClickListener {
                Log.d("VideoRVAdapter", "Meme ID for settings click: ${videoData.memeId}")

                VideoOptionsBottomSheetFragment.newInstance(videoData.s3Url, videoData.memeId).show(fragmentManager, "videoOptions")
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem.s3Url == newItem.s3Url
        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem == newItem
    }
}


//class VideoRVAdapter(private val fragmentManager: FragmentManager,
//                     private val onVideoClick: (String) -> Unit) :
//    ListAdapter<String, VideoRVAdapter.VideoViewHolder>(DiffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
//        val binding = ItemLockerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return VideoViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
//        val videoUrl = getItem(position)
//        holder.bind(videoUrl)
//    }
//
//    inner class VideoViewHolder(private val binding: ItemLockerVideoBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(videoUrl: String) {
//            binding.thumnailImg.setOnClickListener { onVideoClick(videoUrl) }
//            binding.settingImg.setOnClickListener {
//                VideoOptionsBottomSheetFragment.newInstance(videoUrl).show(fragmentManager, "videoOptions")
//            }
//
//        }
//    }
//
//    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
//        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
//        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
//    }
//}