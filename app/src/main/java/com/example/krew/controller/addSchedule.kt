package com.example.krew.controller

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.krew.R
import com.example.krew.adapter.ScheduleAdapter
import com.example.krew.databinding.ActivityAddScheduleBinding
import com.example.krew.model.GroupItem
import com.google.android.libraries.places.api.Places

class addSchedule : AppCompatActivity() {
    lateinit var binding:ActivityAddScheduleBinding
    lateinit var ScheduleAdapter:ScheduleAdapter
    //startdate enddate 예외처리를 위한 haspmap 자료구조
    var startinfo = HashMap<String,Int>()
    var itemarr = ArrayList<GroupItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val apikey = getString(R.string.apiKey)


        Log.i("OnCreateStartAddScheduleActivity","OnCreateStartAddScheduleActivity")
        if(apikey.isEmpty()){
            Toast.makeText(this,"API is not exist",Toast.LENGTH_SHORT).show()
            return
        }
        if(!Places.isInitialized()){
            Places.initialize(applicationContext,apikey)
        }
        binding.ScheduleName.setText(scheduleVar.Name)
        binding.startDateBtn1.setText(scheduleVar.endDate)
        binding.startDateBtn2.setText(scheduleVar.endDateHour)
        binding.endDateBtn1.setText(scheduleVar.endDate)
        binding.endDateBtn2.setText(scheduleVar.endDateHour)
        binding.LocationName.setText(scheduleVar.locationName)
        binding.toggleButton.isChecked = scheduleVar.isChecked

        initRecyclerView()
        initLayout()
    }

    fun initRecyclerView(){
        for(i in 1..10){
            itemarr.add(GroupItem(i.toString(),i.toString(),false))
        }
        ScheduleAdapter = ScheduleAdapter(itemarr)
        ScheduleAdapter.itemClickListener = object:ScheduleAdapter.OnItemClickListener{
            override fun OnItemClick(data: GroupItem, position: Int) {
                data.check = !data.check
                ScheduleAdapter.notifyDataSetChanged()
            }
        }
        binding.crewRecyclerView.adapter = ScheduleAdapter
        binding.crewRecyclerView.layoutManager = GridLayoutManager(this,3)
    }
    @SuppressLint("SetTextI18n")
    fun initLayout(){
        binding.apply {
            backBtn.setOnClickListener {
                //spinner 구현
                clearVar()
                val intent = Intent(this@addSchedule, MainActivity::class.java)
                startActivity(intent)
            }
            checkBtn.setOnClickListener{
                //해당 부분에 main calendar View로 돌아가는 intent문 작성
                //일정 db에 업데이트하는 문 추가 작성
            }
            startDate.setOnClickListener {
                var str:String?=""
                val c: java.util.Calendar = java.util.Calendar.getInstance() // Calendar 객체로 날짜 얻어오기
                val y: Int = c.get(java.util.Calendar.YEAR)
                val month: Int = c.get(java.util.Calendar.MONTH)
                val d: Int = c.get(java.util.Calendar.DAY_OF_MONTH)
                val h: Int = c.get(java.util.Calendar.HOUR_OF_DAY)
                val minute: Int = c.get(java.util.Calendar.MINUTE)

                // 날짜 대화상자 객체 생성
                if(!toggleButton.isChecked){
                    @SuppressWarnings("deprecation")
                    val timePickerDialog = TimePickerDialog(this@addSchedule,android.R.style.Theme_Holo_Light_Dialog,
                        { view, hourOfDay, minute ->
                            startDateBtn2.setText("$hourOfDay:$minute")
                            //전후 값 비교해서 판별하는 구문 추가
                        }, h, minute, true
                    ).show()
                }

                @SuppressWarnings("deprecation")
                val datePickerDialog = DatePickerDialog(
                    this@addSchedule,android.R.style.Theme_Holo_Light_Dialog,
                    { view, year, month, dayOfMonth ->
                        startDateBtn1.setText("$year. ${month+1}. $dayOfMonth")
                    },
                    y, month, d
                ).show()
            }

            endDate.setOnClickListener {
                var str:String?=""
                val c: java.util.Calendar = java.util.Calendar.getInstance() // Calendar 객체로 날짜 얻어오기
                val y: Int = c.get(java.util.Calendar.YEAR)
                val month: Int = c.get(java.util.Calendar.MONTH)
                val d: Int = c.get(java.util.Calendar.DAY_OF_MONTH)
                val h: Int = c.get(java.util.Calendar.HOUR_OF_DAY)
                val minute: Int = c.get(java.util.Calendar.MINUTE)

                // 날짜 대화상자 객체 생성
                if(!toggleButton.isChecked){
                    @SuppressWarnings("deprecation")
                    val timePickerDialog = TimePickerDialog(this@addSchedule,android.R.style.Theme_Holo_Light_Dialog,
                        { view, hourOfDay, minute ->
                            endDateBtn2.setText("$hourOfDay:$minute")

                        }, h, minute, true
                    ).show()
                }
                @SuppressWarnings("deprecation")
                val datePickerDialog = DatePickerDialog(
                    this@addSchedule,android.R.style.Theme_Holo_Light_Dialog,
                    { view, year, month, dayOfMonth ->
                        endDateBtn1.setText("$year. ${month+1}. $dayOfMonth")
                    },
                    y, month, d
                ).show()
            }
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                scheduleVar.isChecked = isChecked
                if(isChecked){
                    Toast.makeText(this@addSchedule,"check됨",Toast.LENGTH_SHORT).show()
                    startDateBtn2.setText("")
                    endDateBtn2.setText("")
                }else{
                    Toast.makeText(this@addSchedule,"check안됨",Toast.LENGTH_SHORT).show()
                    startDateBtn1.setText("")
                    startDateBtn2.setText("")
                    endDateBtn1.setText("")
                    endDateBtn2.setText("")
                }
            }
            LocationName.setOnClickListener {
                backupDateBeforeIntent()
                val intent = Intent(this@addSchedule, ProgrammaticAutocompleteGeocodingActivity::class.java)
                startActivity(intent)
                finish()
                }
            if(intent.hasExtra("formattedAddress")){
                val formattedAddr = intent.getStringExtra("formattedAddress")
                LocationName.setText(formattedAddr.toString())
            }
        }
    }
    fun backupDateBeforeIntent(){
        scheduleVar.Name = binding.ScheduleName.text.toString()
        scheduleVar.locationName = binding.LocationName.text.toString()
        scheduleVar.startDate = binding.startDateBtn1.text.toString()
        scheduleVar.endDate = binding.endDateBtn1.text.toString()
        scheduleVar.startDateHour = binding.startDateBtn2.text.toString()
        scheduleVar.endDateHour = binding.endDateBtn2.text.toString()
    }

    fun clearVar(){
        scheduleVar.Name = ""
        scheduleVar.locationName = ""
        scheduleVar.startDate = ""
        scheduleVar.startDateHour = ""
        scheduleVar.endDate = ""
        scheduleVar.endDateHour = ""
    }
}