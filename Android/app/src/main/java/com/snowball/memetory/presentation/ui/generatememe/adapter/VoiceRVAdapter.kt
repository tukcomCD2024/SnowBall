package com.snowball.memetory.presentation.ui.generatememe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ItemChooseVoiceBinding
import de.hdodenhof.circleimageview.CircleImageView

class VoiceRVAdapter(private val dataList: List<String>): RecyclerView.Adapter<VoiceRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceRVAdapter.ViewHolder {
        val view = DataBindingUtil.inflate<ItemChooseVoiceBinding>(LayoutInflater.from(parent.context), R.layout.item_choose_voice, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoiceRVAdapter.ViewHolder, position: Int) {
        holder.name.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(binding: ItemChooseVoiceBinding): RecyclerView.ViewHolder(binding.root) {
        val profile: CircleImageView = binding.profileImg
        val name: AppCompatTextView = binding.nameText
        val voice: AppCompatTextView = binding.voiceText

    }
}