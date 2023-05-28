package com.example.krew

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.DayInfoRowBinding
import com.example.krew.databinding.ListItemDayBinding

class AdapterDayInfo (val items:ArrayList<DayInfo>):RecyclerView.Adapter<AdapterDayInfo.ViewHolder>(){

    inner class ViewHolder(val binding: DayInfoRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDayInfo.ViewHolder {
        val view = DayInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterDayInfo.ViewHolder, position: Int) {
        holder.binding.planName.setText(items[position].name)
        holder.binding.planTime.setText(items[position].time)
        holder.binding.plan.setBackgroundColor(items[position].color)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}