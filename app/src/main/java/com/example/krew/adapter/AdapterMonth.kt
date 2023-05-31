package com.example.krew.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.ListItemMonthBinding


import java.util.*

class AdapterMonth: RecyclerView.Adapter<AdapterMonth.MonthView>() {

    val center = Int.MAX_VALUE /2
    private var calendar = Calendar.getInstance()

    inner class MonthView(val monthBinding: ListItemMonthBinding): RecyclerView.ViewHolder(monthBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthView {
        val view = ListItemMonthBinding.inflate(LayoutInflater.from(parent.context))
        return MonthView(view)
    }

    override fun onBindViewHolder(holder: MonthView, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - center)


        holder.monthBinding.itemMonthText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
        val tempMonth = calendar.get(Calendar.MONTH)

        var dayList: MutableList<Date> = MutableList(6 * 7) { Date() }
        for(i in 0..5) {
            for(k in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1-calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList[i * 7 + k] = calendar.time
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        val dayListManager = GridLayoutManager(holder.monthBinding.root.context, 7)
        val dayListAdapter = AdapterDay(tempMonth, dayList)

        holder.monthBinding.itemMonthDayList.apply{
            layoutManager = dayListManager
            adapter = dayListAdapter
        }
    }


    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}