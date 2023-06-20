package com.example.krew.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krew.adapter.AdapterDayInfo
import com.example.krew.databinding.DayInfoBinding
import com.example.krew.model.DayInfo
import com.example.krew.model.Schedule

class DayInfoActivity : AppCompatActivity(){
    lateinit var binding: DayInfoBinding
    lateinit var adapterDayInfo:AdapterDayInfo
    val dayInfoData:ArrayList<DayInfo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()    //click돼서 넘어온 data를 어떻게 받아와서 처리해야되는데
        initRecyclerView()
    }

    fun initData(){
        val today = intent.getStringExtra("today")
        val dayInfo_list = intent.getSerializableExtra("dayInfo_list") as ArrayList<DayInfo>
        binding.dayText.setText(today)

        for (day in dayInfo_list){
            dayInfoData.add(DayInfo(day.title, day.location, day.time, day.color))
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.addBtn.setOnClickListener {
            val intent = Intent(this@DayInfoActivity, AddSchedule::class.java)
            intent.putExtra("selected_date", today)
            startActivity(intent)
            finish()
        }
    }

    fun initRecyclerView() {
        binding.dayInfoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapterDayInfo = AdapterDayInfo(dayInfoData)   //여기에 어떤 정보를 넘겨야 되는 거지??
        binding.dayInfoRecyclerView.adapter = adapterDayInfo
    }
}