package com.example.krew.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.R
import com.example.krew.model.DayInfo
import com.example.krew.databinding.DayInfoRowBinding

class AdapterDayInfo (val items:ArrayList<DayInfo>):RecyclerView.Adapter<AdapterDayInfo.ViewHolder>(){

    inner class ViewHolder(val binding: DayInfoRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DayInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawable = holder.binding.root.context.getDrawable(R.drawable.bg_radius_100dp)
        //val drawable = context
        if (drawable != null) {
            drawable.setColorFilter(BlendModeColorFilter(items[position].color, BlendMode.SRC_ATOP))
        }



        holder.binding.planName.setText(items[position].location)
        holder.binding.planTime.setText(items[position].time)
        holder.binding.plan.setBackground(drawable)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}