package com.snowball.memetory.presentation.ui.generatememe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ItemChooseVoiceBinding
import com.snowball.memetory.domain.model.voice.Voice
import de.hdodenhof.circleimageview.CircleImageView

//class VoiceRVAdapter(
//    private val voices: List<Voice>,
//    private val onVoiceSelected: (Voice) -> Unit
//) : RecyclerView.Adapter<VoiceRVAdapter.ViewHolder>() {

class VoiceRVAdapter(
    private val voices: Voice,
    private val onVoiceSelected: (Voice) -> Unit
) : RecyclerView.Adapter<VoiceRVAdapter.ViewHolder>() {

    var selectedPosition = -1

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemChooseVoiceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_choose_voice,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val voice = voices[position]
        val voice = voices
        holder.binding.nameText.text = voice.name
        holder.binding.itemRadioBtn.isChecked = position == selectedPosition
        holder.binding.itemRadioBtn.setOnClickListener {
            if (selectedPosition != position) {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
//                onVoiceSelected(voices[selectedPosition])
                onVoiceSelected(voices)
            }
        }
    }

//    override fun getItemCount(): Int = voices.size
    override fun getItemCount(): Int = 1
    inner class ViewHolder(val binding: ItemChooseVoiceBinding) : RecyclerView.ViewHolder(binding.root)
}
