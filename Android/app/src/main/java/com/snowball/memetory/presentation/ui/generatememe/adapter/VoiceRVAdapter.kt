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
import de.hdodenhof.circleimageview.CircleImageView

class VoiceRVAdapter(private val dataList: List<String>, private val itemClick: VoiceRVAdapter.OnItemClickListener): RecyclerView.Adapter<VoiceRVAdapter.ViewHolder>() {

    var selectedPosition = -1

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoiceRVAdapter.ViewHolder {
        val view = DataBindingUtil.inflate<ItemChooseVoiceBinding>(LayoutInflater.from(parent.context), R.layout.item_choose_voice, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoiceRVAdapter.ViewHolder, position: Int) {
        holder.name.text = dataList[position]
        holder.radioBtn.isChecked = position == selectedPosition

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(binding: ItemChooseVoiceBinding): RecyclerView.ViewHolder(binding.root) {
        val name: AppCompatTextView = binding.nameText
        val radioBtn: AppCompatRadioButton = binding.itemRadioBtn
        init {
            radioBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {

                    if (selectedPosition != adapterPosition) {
                        val previousSelectedPosition = selectedPosition
                        selectedPosition = adapterPosition
                        notifyItemChanged(previousSelectedPosition)
                        notifyItemChanged(selectedPosition)
                        itemClick.onItemClick(itemView ,selectedPosition)
                    }
                }
            }

        }
    }
}