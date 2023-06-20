package com.example.krew.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.loader.ResourcesProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.R
import com.example.krew.databinding.GroupItemBinding
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem
import kotlin.coroutines.CoroutineContext

class GroupRVAdapter(val context: Context,
                     private val dataList: ArrayList<Calendar>,
) : RecyclerView.Adapter<GroupRVAdapter.ItemViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(position:Int)

    }

    var itemClickListener:OnItemClickListener?= null

    inner class ItemViewHolder(val binding:GroupItemBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.tvGroupName.setOnClickListener{
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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val listposition = dataList[position]
        holder.binding.apply {
            tvGroupName.text = listposition.name
            tvGroupHead.text = listposition.admin

            Log.d("ColorCode", "color = ${listposition.label}, ${R.color.color_gr4}")
            tagColor.backgroundTintList = context.resources.getColorStateList(listposition.label)
            //tagColor.backgroundTintList = ColorStateList.valueOf(listposition.label)

            //rvGroup.setBackgroundColor(resources.getColor(colorCode, null))

        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}