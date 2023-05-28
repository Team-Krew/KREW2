package com.example.krew.group

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.GroupItemBinding
import com.example.krew.databinding.MemberItemBinding
import java.lang.reflect.Member

class GroupRVAdapter(
    private val dataList: ArrayList<GroupItem>,
) : RecyclerView.Adapter<GroupRVAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(val binding:
                               GroupItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: GroupItem){
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
            binding.tvGroupName.text = listposition.group_name
            binding.tvGroupHead.text = listposition.group_head
            binding.swGroup.isChecked = listposition.check
        }

    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}