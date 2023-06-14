package com.example.krew.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krew.adapter.AdapterDayInfo
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.DayInfoBinding
import com.example.krew.model.DayInfo

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
        val receivedIntent = intent // AdapterDay에서 전달받은 intent

        binding.dayText.setText(intent.getStringExtra("today"))

        dayInfoData.add(DayInfo("예를 들면 헬스장", "08:00", Color.CYAN))
        dayInfoData.add(DayInfo("건대입구", "14:00", Color.MAGENTA))
        dayInfoData.add(DayInfo("용현이집", "02:30", Color.YELLOW))
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.addBtn.setOnClickListener {
            val intent = Intent(this@DayInfoActivity,addSchedule::class.java)
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