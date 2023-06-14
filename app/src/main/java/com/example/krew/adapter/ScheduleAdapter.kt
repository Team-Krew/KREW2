package com.example.krew.adapter

import android.graphics.Color
import android.os.Build.VERSION_CODES.P
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.R
import com.example.krew.databinding.ActivityAddScheduleBinding
import com.example.krew.databinding.MemberListBinding
import com.example.krew.model.Calendar
import com.example.krew.model.DayInfo
import com.example.krew.model.GroupItem
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.coroutines.NonDisposableHandle.parent

class ScheduleAdapter(val items:ArrayList<GroupItem>):RecyclerView.Adapter<ScheduleAdapter.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(val binding: MemberListBinding) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.CrewGrid.setOnClickListener{
                itemClickListener?.OnItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHolder {
        val view = MemberListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recrewName.text = items[position].group_name.toString()
        if(items[position].check){
            holder.binding.recrewImage.setImageResource(R.drawable.member_list_checkedimage)
            holder.binding.recrewName.setTextColor(Color.parseColor("#FFd9d9d9"))
            Log.i("","${items[position].group_name} checked")
        }else{
            holder.binding.recrewImage.setImageResource(R.drawable.memberlist_not_checked_image)
            holder.binding.recrewName.setTextColor(Color.parseColor("#FF000000"))
            Log.i("","${items[position].group_name} unchecked")
        }
    }

}