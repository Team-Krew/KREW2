package com.example.krew.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.MemberItemBinding
import com.example.krew.model.MemberItem

class MemberRVAdapter(
    private val dataList: ArrayList<MemberItem>,
) : RecyclerView.Adapter<MemberRVAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(val binding:
                               MemberItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MemberItem){
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemViewHolder {
        val binding = MemberItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val listposition = dataList[position]
        holder.bind(dataList[position])

        holder.apply {
            binding.tvName.text = listposition.name
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}