package com.example.krew.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.GroupItemBinding
import com.example.krew.databinding.MemberItemBinding
import com.example.krew.model.Calendar
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class GroupFBAdapter(options: FirebaseRecyclerOptions<Calendar>)
    : FirebaseRecyclerAdapter<Calendar, GroupFBAdapter.ItemViewHolder>(options){

    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClickListener:OnItemClickListener?= null

    inner class ItemViewHolder(val binding:GroupItemBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemViewHolder {
        val binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model:Calendar) {
        holder.binding.apply{
            Log.e("Firebase communication", model.name)
            tvGroupName.text = model.name
            tvGroupHead.text = model.admin
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}