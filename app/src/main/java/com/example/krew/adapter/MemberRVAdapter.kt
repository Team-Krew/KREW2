package com.example.krew.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.MemberItemBinding

class MemberRVAdapter(private val users:ArrayList<String>)
    : RecyclerView.Adapter<MemberRVAdapter.ItemViewHolder>(){

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.apply{
            tvName.text = users[position]
            //Log.e("Recycling", model.name)
            //tvName.text = model.Participant?.get(position)?.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return users.size
    }
}