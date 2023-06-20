package com.example.krew.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.ApplicationClass
import com.example.krew.controller.DayInfoActivity
import com.example.krew.databinding.ListItemDayBinding
import com.example.krew.model.Schedule
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDay(val tempMonth:Int, val dayList: MutableList<Date>): RecyclerView.Adapter<AdapterDay.DayView>() {
    val ROW = 6
    inner class DayView(val dayBinding: ListItemDayBinding): RecyclerView.ViewHolder(dayBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDay.DayView {
        var view = ListItemDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DayView(view)
    }

    override fun onBindViewHolder(holder: DayView, position: Int) {
        //날짜 클릭시 이벤트 처리
        val today = formatDate(dayList[position])
        println("today: " + today)
        val schedule_list =  ArrayList<Schedule>()
        for (cal in ApplicationClass.cur_calendar_list){
//            println(cal)
            val temp_schedule_list = cal.schedule_list as ArrayList<Schedule>
            for (t in temp_schedule_list){
                println("target_date: " + t.date)
                if(t.date == today){
                    schedule_list.add(t)
                }
            }
        }

//        println(schedule_list)

        holder.dayBinding.itemDayLayout.setOnClickListener {
            val intent = Intent(holder.dayBinding.root.context, DayInfoActivity::class.java)
            //날짜 가공
            val today = formatDate(dayList[position])
            intent.putExtra("today", today)
            holder.dayBinding.root.context.startActivity(intent)

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
    fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return format.format(date)
    }

    override fun getItemCount(): Int {
        return ROW * 7
    }
}