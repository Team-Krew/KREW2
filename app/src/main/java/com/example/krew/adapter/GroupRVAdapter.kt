package com.example.krew.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.GroupItemBinding
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem

class GroupRVAdapter(
    private val dataList: ArrayList<Calendar>,
) : RecyclerView.Adapter<GroupRVAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(val binding:
                               GroupItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: Calendar){
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemViewHolder {
        val binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val listposition = dataList[position]
        holder.bind(dataList[position])

        holder.apply {
            binding.tvGroupName.text = listposition.name
            binding.tvGroupHead.text = listposition.admin
        }

    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}