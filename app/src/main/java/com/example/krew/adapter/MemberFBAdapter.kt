package com.example.krew.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.MemberItemBinding
import com.example.krew.model.Calendar
import com.example.krew.model.MemberItem
import com.example.krew.model.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MemberFBAdapter(options: FirebaseRecyclerOptions<Calendar>)
    : FirebaseRecyclerAdapter<Calendar, MemberFBAdapter.ItemViewHolder>(options){

    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }

    var itemClickListener:OnItemClickListener?= null

    inner class ItemViewHolder(val binding:MemberItemBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ItemViewHolder {
        val binding = MemberItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model:Calendar) {
        holder.binding.apply{
            Log.e("Recycling", model.name)
            //tvName.text = model.Participant?.get(position)?.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}