package com.example.krew.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.DayInfoRowBinding

class AdapterDayInfo (val items:ArrayList<DayInfo>):RecyclerView.Adapter<AdapterDayInfo.ViewHolder>(){

    inner class ViewHolder(val binding: DayInfoRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DayInfoRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.planName.setText(items[position].name)
        holder.binding.planTime.setText(items[position].time)
        holder.binding.plan.setBackgroundColor(items[position].color)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}