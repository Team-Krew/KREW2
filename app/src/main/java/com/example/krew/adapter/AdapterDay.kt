package com.example.krew.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.R
import com.example.krew.controller.MainActivity
import com.example.krew.controller.addSchedule
import com.example.krew.databinding.ListItemDayBinding
import java.util.*

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    inner class DayView(val dayBinding: ListItemDayBinding): RecyclerView.ViewHolder(dayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDay.DayView {
        var view = ListItemDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.dayBinding.itemDayLayout.setOnClickListener {
            Toast.makeText(holder.dayBinding.root.context, "${dayList[position]}", Toast.LENGTH_SHORT).show()
        }


        holder.dayBinding.itemDayText.text = dayList[position].date.toString()
        holder.dayBinding.itemDayText.setTextColor(when(position%7){
            0 -> Color.RED
            6 -> Color.BLUE
            else -> Color.BLACK
        })

        if(tempMonth != dayList[position].month) {
            holder.dayBinding.itemDayText.alpha = 0.4f
        }
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}