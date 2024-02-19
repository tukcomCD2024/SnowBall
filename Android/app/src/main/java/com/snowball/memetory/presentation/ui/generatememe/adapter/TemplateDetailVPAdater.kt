package com.snowball.memetory.presentation.ui.generatememe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ItemTemplateDetailBinding

class TemplateDetailVPAdater(private val imgRes: ArrayList<Int>)
    : RecyclerView.Adapter<TemplateDetailVPAdater.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemplateDetailVPAdater.ViewHolder {
        val view = DataBindingUtil.inflate<ItemTemplateDetailBinding>(LayoutInflater.from(parent.context), R.layout.item_template_detail, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateDetailVPAdater.ViewHolder, position: Int) {
        holder.img.setImageResource(imgRes[position])
    }

    override fun getItemCount(): Int {
        return imgRes.size
    }

    inner class ViewHolder(binding : ItemTemplateDetailBinding): RecyclerView.ViewHolder(binding.root) {
        val img = binding.templateImg
    }
}