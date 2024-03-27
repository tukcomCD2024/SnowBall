package com.snowball.memetory.presentation.ui.generatememe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ItemTemplateBinding

class TemplateRVAdater(private val imgRes: ArrayList<Int>, private val itemClick: OnItemClickListener)
    : RecyclerView.Adapter<TemplateRVAdater.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemplateRVAdater.ViewHolder {
        val view = DataBindingUtil.inflate<ItemTemplateBinding>(LayoutInflater.from(parent.context), R.layout.item_template, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateRVAdater.ViewHolder, position: Int) {
//        holder.img.setImageResource(imgRes[position])
    }

    override fun getItemCount(): Int {
        return imgRes.size
    }

    inner class ViewHolder(binding : ItemTemplateBinding): RecyclerView.ViewHolder(binding.root) {
        val img = binding.templateImg

        init {
            itemView.setOnClickListener {
                itemClick.onItemClick(it, adapterPosition)

            }
        }
    }
}