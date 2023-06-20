package com.example.krew.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.adapter.ScheduleAdapter
import com.example.krew.controller.scheduleVar.endDate
import com.example.krew.controller.scheduleVar.locationAddr
import com.example.krew.controller.scheduleVar.placename
import com.example.krew.controller.scheduleVar.startDate
import com.example.krew.databinding.ActivityAddScheduleBinding
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem
import com.example.krew.model.Schedule
import com.google.android.libraries.places.api.Places
import com.google.android.play.integrity.internal.f
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class AddSchedule : AppCompatActivity() {
    lateinit var binding:ActivityAddScheduleBinding
    lateinit var layoutManager:LayoutManager
    lateinit var ScheduleAdapter:ScheduleAdapter
    lateinit var today: String
    lateinit var binding: ActivityAddScheduleBinding
    lateinit var layoutManager: LayoutManager
    lateinit var ScheduleAdapter: ScheduleAdapter
    var itemarr = ArrayList<GroupItem>()
    var calarr = ArrayList<Calendar>()
    var checked_groupItems = ArrayList<GroupItem>()
    lateinit var apikey :String
    private val AUTOCOMPLETE_REQUEST_CODE = 1


    var formattedAddress:String ?= ""
    var place:String ?= ""
    var selected_date:String ?= ""

    lateinit var today: String

    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                formattedAddress = it.data?.getStringExtra("formattedAddress")
                place = it.data?.getStringExtra("place")
                selected_date = it.data?.getStringExtra("selected_date")


                Log.e("Firebase Communication", "in addschedule $formattedAddress, $place, $selected_date")

                placename = place!!
                locationAddr = formattedAddress!!

                binding.apply {
                    LocationAddr.text = locationAddr
                    LocationName.text = placename
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.i("checkONCreate","checkONCreate")
        apikey = getString(R.string.apiKey)
        Log.i("OnCreateStartAddScheduleActivity","OnCreateStartAddScheduleActivity")
        if(apikey.isEmpty()){
            Toast.makeText(this,"API is not exist",Toast.LENGTH_SHORT).show()
            return
        val apikey = getString(R.string.apiKey)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apikey)
        }
        binding.ScheduleName.setText(scheduleVar.Name)
        binding.LocationAddr.setText(scheduleVar.locationAddr)
        binding.toggleButton.isChecked = scheduleVar.istoggleChecked
        binding.startDateBtn1.setText(startDate)
        binding.startDateBtn2.setText(scheduleVar.startDateHour)
        binding.endDateBtn1.setText(endDate)
        binding.endDateBtn2.setText(scheduleVar.endDateHour)
        binding.LocationName.text = scheduleVar.placename

        today = intent.getStringExtra("selected_date")!!
        initRecyclerView()
        initLayout()
    }

    //데이터 읽어와서 쓰고 수정.
    fun initRecyclerView() {
        layoutManager =
            LinearLayoutManager(this@AddSchedule, LinearLayoutManager.HORIZONTAL, false)
        ScheduleAdapter = ScheduleAdapter(itemarr)
        ScheduleAdapter.itemClickListener = object : ScheduleAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                val target = ScheduleAdapter.items[position]
                if (target.check == false) {
                    if (target !in checked_groupItems) {
                        target.check = true
                        checked_groupItems.add(target)
                        println(checked_groupItems)
                    }
                } else {
                    if (target in checked_groupItems) {
                        target.check = false
                        checked_groupItems.remove(target)
                        println(checked_groupItems)
                    }
                }
                ScheduleAdapter.notifyDataSetChanged()
            }
        }
        binding.crewRecyclerView.adapter = ScheduleAdapter
        binding.crewRecyclerView.layoutManager = layoutManager
    }

    @SuppressLint("SetTextI18n")
    fun initLayout() {
        binding.apply {
            backBtn.setOnClickListener {
                clearVar()
                clearAllBtn()
                val intent = Intent(this@AddSchedule, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            checkBtn.setOnClickListener {
                //해당 부분에 main calendar View로 돌아가는 intent문 작성
                //일정 db에 업데이트하는 1문 추가 작성
                //다 유효값이 들어가 있는 경우에만 firebase에 추가하는 구문 추가
                if (toggleButton.isChecked) {
                    if (ScheduleName.text.isNotEmpty() && startDateBtn1.text.isNotEmpty() &&
                        endDateBtn1.text.isNotEmpty() && LocationAddr.text.isNotEmpty()
                    ) {
                        //여기다가 schedule add하는 과정 추가
                        makeSchedule()
                        var intent = Intent(this@AddSchedule, CheckRegisterActivity::class.java)
                        startActivity(intent)
                        clearVar()
                        clearAllBtn()
                        finish()
                    } else {
                        var str = "올바른 입력이 아닙니다. "
                        if (ScheduleName.text.isEmpty()) {
                            str += "일정 이름, "
                        } else if (startDateBtn1.text.isEmpty()) {
                            str += "출발 날짜,"
                        } else if (endDateBtn1.text.isEmpty()) {
                            str += "도착 날짜, "
                        } else if (LocationAddr.text.isEmpty()) {
                            str += "장소, "
                        }
                        str += "을(를) 작성해주세요."
                        Toast.makeText(this@AddSchedule, str, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (ScheduleName.text.isNotEmpty() && startDateBtn1.text.isNotEmpty() && startDateBtn2.text.isNotEmpty() &&
                        endDateBtn1.text.isNotEmpty() && endDateBtn2.text.isNotEmpty() && LocationAddr.text.isNotEmpty()
                    ) {
                        //여기다가 schedule add하는 과정 추가
                        makeSchedule()
                        makeALARM()
                        var intent = Intent(this@AddSchedule, CheckRegisterActivity::class.java)
                        startActivity(intent)
                        clearVar()
                        clearAllBtn()
                        finish()
                    } else {
                        var str = "올바른 입력이 아닙니다. "
                        if (ScheduleName.text.isEmpty()) {
                            str += "일정 이름, "
                        } else if (startDateBtn1.text.isEmpty() || startDateBtn2.text.isEmpty()) {
                            str += "출발 날짜 및 시간, "
                        } else if (endDateBtn1.text.isEmpty() || endDateBtn2.text.isEmpty()) {
                            str += "도착 날짜 및 시간, "
                        } else if (LocationAddr.text.isEmpty()) {
                            str += "장소, "
                        }
                        str += "을(를) 작성해주세요."
                        Toast.makeText(this@AddSchedule, str, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            startDate.setOnClickListener {
                val c: java.util.Calendar = java.util.Calendar.getInstance() // Calendar 객체로 날짜 얻어오기
                val y: Int = c.get(java.util.Calendar.YEAR)
                val month: Int = c.get(java.util.Calendar.MONTH)
                val d: Int = c.get(java.util.Calendar.DAY_OF_MONTH)
                val h: Int = c.get(java.util.Calendar.HOUR_OF_DAY)
                val minute: Int = c.get(java.util.Calendar.MINUTE)

                @SuppressWarnings("deprecation")
                val datePickerDialog = DatePickerDialog(
                    this@AddSchedule, android.R.style.Theme_Holo_Light_Dialog,
                    { view, year, month, dayOfMonth ->
                        scheduleVar.startDate = "${year}-${month + 1}-${dayOfMonth}"
                        startDateBtn1.text = scheduleVar.startDate
                        // 날짜 대화상자 객체 생성
                        if (!toggleButton.isChecked) {
                            @SuppressWarnings("deprecation")
                            val timePickerDialog = TimePickerDialog(
                                this@AddSchedule, android.R.style.Theme_Holo_Light_Dialog,
                                { view, hourOfDay, minute ->
                                    scheduleVar.startDateHour = "${hourOfDay}:${minute}"
                                    startDateBtn2.text = scheduleVar.startDateHour
                                    //전후 값 비교해서 판별하는 구문 추가
                                }, h, minute, true
                            ).show()
                            Log.i("timepicker", timePickerDialog.toString())
                        }
                    },
                    y, month, d
                ).show()
            }

            endDate.setOnClickListener {
                var str: String? = ""
                val c: java.util.Calendar = java.util.Calendar.getInstance() // Calendar 객체로 날짜 얻어오기
                val y: Int = c.get(java.util.Calendar.YEAR)
                val month: Int = c.get(java.util.Calendar.MONTH)
                val d: Int = c.get(java.util.Calendar.DAY_OF_MONTH)
                val h: Int = c.get(java.util.Calendar.HOUR_OF_DAY)
                val minute: Int = c.get(java.util.Calendar.MINUTE)

                @SuppressWarnings("deprecation")
                val datePickerDialog = DatePickerDialog(
                    this@AddSchedule, android.R.style.Theme_Holo_Light_Dialog,
                    { view, year, month, dayOfMonth ->
                        scheduleVar.endDate = "$year-${month + 1}-$dayOfMonth"
                        if (scheduleVar.endDate.compareTo(scheduleVar.startDate) == 0 || scheduleVar.endDate.compareTo(
                                scheduleVar.startDate
                            ) > 0
                        ) {
                            endDateBtn1.text = scheduleVar.endDate
                            if (!toggleButton.isChecked) {
                                @SuppressWarnings("deprecation")
                                val timePickerDialog = TimePickerDialog(
                                    this@AddSchedule, android.R.style.Theme_Holo_Light_Dialog,
                                    { view, hourOfDay, minute ->
                                        scheduleVar.endDateHour = "$hourOfDay:$minute"
                                        //동일날짜
                                        if (scheduleVar.endDate.compareTo(scheduleVar.startDate) == 0) {
                                            Log.i(
                                                "scheduleVar.endDateHour.compareTo(scheduleVar.startDateHour)",
                                                scheduleVar.endDateHour.compareTo(scheduleVar.startDateHour)
                                                    .toString()
                                            )
                                            if (scheduleVar.endDateHour.compareTo(scheduleVar.startDateHour) > 0 || scheduleVar.endDateHour.compareTo(
                                                    scheduleVar.startDateHour
                                                ) == 0
                                            ) {
                                                endDateBtn2.text = scheduleVar.endDateHour
                                            } else {
                                                Toast.makeText(
                                                    this@AddSchedule,
                                                    "올바르지 않은 형식입니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                clearendBtn()
                                            }
                                            //endDate가 더 큰 값
                                        } else if (scheduleVar.endDate.compareTo(scheduleVar.startDate) > 0) {
                                            endDateBtn2.text = scheduleVar.endDateHour
                                            //endDate가 더 작은 값
                                        } else {
                                            clearendBtn()
                                        }

                                    }, h, minute, true
                                ).show()
                            }
                        } else {
                            Toast.makeText(this@AddSchedule, "올바르지 않은 입력입니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    y, month, d
                ).show()
            }
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                scheduleVar.istoggleChecked = isChecked
                if (isChecked) {
                    startDateBtn2.setText("")
                    endDateBtn2.setText("")
                } else {
                    startDateBtn1.setText("")
                    startDateBtn2.setText("")
                    endDateBtn1.setText("")
                    endDateBtn2.setText("")
                }
            }
            LocationAddr.setOnClickListener {
                backupDateBeforeIntent()
                val intent =
                    Intent(this@AddSchedule, ProgrammaticAutocompleteGeocodingActivity::class.java)
                intent.putExtra("selected_date", today)
                activityResultLauncher.launch(intent)
                
                startActivity(intent)
                finish()
                }
            addMemberButton.setOnClickListener {
                val intent = Intent(this@AddSchedule,GroupActivity::class.java)
                startActivity(intent)
            }
        }
    }
    
    fun makeSchedule(){
        val startDate1 = binding.startDateBtn1.text.toString()
        val startDate2 = binding.startDateBtn2.text.toString()
        val Schedule = Firebase.database.getReference("Schedule")
        val db = Firebase.database.reference
        var sNum = 0;
        //스케줄 id->SNum calendar_list->groupITem에서 checktrue인 group_id를 가지고있는 캘린더들 가져오기
        //title->name 날짜: startdate, startdatehour(if종일, 00:00), place->string
        val mDatabase = Firebase.database.getReference("Calendar")
        val checked_items = checked_groupItems

        if (checked_items != null) {
            for (group_item in checked_items) {
                for (cal in ApplicationClass.cur_calendar_list) {
                    if (cal.calendar_id == group_item.group_id) {
                        calarr.add(cal)
                        break
                    }
                }
            }
        }

        db.child("SNum").get().addOnSuccessListener {
            sNum = it.value.toString().toInt()
            val sch = Schedule(
                sNum.toString(),
                binding.ScheduleName.text.toString(),
                today,
                startDate1,
                binding.LocationAddr.text.toString()
            )
            Schedule.child(sNum.toString()).setValue(sch)
            val user_calendar = ApplicationClass.cur_calendar_list
            for (uCal in user_calendar){
                for (cal in calarr){
                    if (uCal.calendar_id == cal.calendar_id){
                        if (uCal.schedule_list == null){
                            uCal.schedule_list = ArrayList()
                            uCal.schedule_list!!.add(sch)
                        }else{
                            uCal.schedule_list!!.add(sch)
                        }
                    }
                }

                mDatabase.child(uCal.calendar_id).setValue(uCal)
            }

            db.child("SNum").setValue(sNum + 1)
            Toast.makeText(this@AddSchedule, "등록되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    fun backupDateBeforeIntent() {
        scheduleVar.Name = binding.ScheduleName.text.toString()
        scheduleVar.locationAddr = binding.LocationAddr.text.toString()
        startDate = binding.startDateBtn1.text.toString()
        endDate = binding.endDateBtn1.text.toString()
        scheduleVar.startDateHour = binding.startDateBtn2.text.toString()
        scheduleVar.endDateHour = binding.endDateBtn2.text.toString()
    }

    fun clearVar() {
        scheduleVar.Name = ""
        scheduleVar.locationAddr = ""
        startDate = ""
        scheduleVar.startDateHour = ""
        endDate = ""
        scheduleVar.endDateHour = ""
        scheduleVar.istoggleChecked = false
        scheduleVar.placename = ""
    }

    fun clearstartBtn() {
        binding.apply {
            startDateBtn1.text = ""
            startDateBtn2.text = ""
        }
    }

    fun clearendBtn() {
        binding.apply {
            endDateBtn1.text = ""
            endDateBtn2.text = ""
        }
    }

    fun clearAllBtn() {
        clearstartBtn()
        clearendBtn()
    }

    override fun onBackPressed() {
        clearVar()
        finish()
    }

    override fun onPause() {
        super.onPause()
        Log.i("onPause", "onPause")
    }

    override fun onResume() {
        //sharedPreference로 값 가져와서
        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {

            ApplicationClass.updateCalendarList()

            val calendars =
                ApplicationClass.sSharedPreferences.getString("calendars", null)?.split(",")
            //GroupActivity에서 일정 추가하고 다시 돌아왔을때 ApplicationClass에 데이터 추가가 안돼서 오류가 나옴.
            if(itemarr==null) {
                itemarrAdd()
            }else if(calendars!!.size!=itemarr.size){
                itemarr.clear()
                itemarrAdd()
            }
            layoutManager = LinearLayoutManager(this@AddSchedule,
                LinearLayoutManager.HORIZONTAL, false)
            ScheduleAdapter.notifyDataSetChanged()
        }
    }
    fun itemarrAdd(){
        val calendar_list = ApplicationClass.cur_calendar_list
        println(calendar_list)
        for (cal in calendar_list!!) {
            itemarr.add(
                GroupItem(
                    cal.calendar_id,
                    cal.name,
                    cal.admin.toString(),
                    resources.getColor(cal.label, null),
                    false
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

//backup

//firebaserecyclerview
//            rdb = Firebase.database.getReference("Calendar")
//            val query = rdb
//            val option = FirebaseRecyclerOptions.Builder<Calendar>()
//                .setQuery(query, Calendar::class.java)
//                .build()
//
//            ScheduleAdapter = ScheduleAdapter(option)
//            ScheduleAdapter.itemClickListener = object : ScheduleAdapter.OnItemClickListener {
//                override fun OnItemClick(position: Int) {
//                    ScheduleAdapter.getItem(position)
//                }
//
//            }
//@SuppressLint("NotifyDataSetChanged")
//override fun onStart() {
//    super.onStart()
//    layoutManager = LinearLayoutManager(this@AddSchedule,
//        LinearLayoutManager.HORIZONTAL, false)
//    val mDatabase = Firebase.database.getReference("Calendar")
//    val calendars =
//        ApplicationClass.sSharedPreferences.getString("calendars", null)?.split(",")
//    Log.e("Firebase communication in addschedule", "${calendars?.size}")
//    if (calendars != null) {
//        for (id in calendars) {
//            mDatabase.child(id).get().addOnSuccessListener {
//                val cal: Calendar
//                cal = it.getValue<Calendar>() as Calendar
//                itemarr.add(GroupItem(
//                    cal.calendar_id,
//                    cal.name,
//                    cal.admin.toString(),
//                    resources.getColor(cal.label.toInt(), null),
//                    false
//                ))
//                ScheduleAdapter.notifyDataSetChanged()
//            }
//        }
//    }
//}