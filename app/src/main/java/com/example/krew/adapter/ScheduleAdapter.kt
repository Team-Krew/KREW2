package com.example.krew.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.R
import com.example.krew.databinding.ActivityAddScheduleBinding
import com.example.krew.databinding.MemberListBinding
import com.example.krew.model.GroupItem
import kotlinx.coroutines.NonDisposableHandle.parent

class ScheduleAdapter(val items:ArrayList<GroupItem>)
    : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>()
{

    interface OnItemClickListener{
        fun OnItemClick(data:GroupItem, position: Int)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: MemberListBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.CrewGrid.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        val view = MemberListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recrewName.setText(items[position].group_name)
        Log.i("checkgroupITEMS",holder.binding.recrewName.text.toString())
        if(items[position].check){
            holder.binding.recrewImage.setImageResource(R.drawable.member_list_checkedimage)
            holder.binding.recrewName.setTextColor(0x888)
        }else{
            holder.binding.recrewImage.setImageResource(R.drawable.memberlist_not_checked_image)
            holder.binding.recrewName.setTextColor(0xFFF)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}