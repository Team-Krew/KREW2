package com.example.krew.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.ApplicationClass
import com.example.krew.ApplicationClass.Companion.cur_user
import com.example.krew.R
import com.example.krew.adapter.AdapterDay
import com.example.krew.adapter.AdapterDayInfo
import com.example.krew.adapter.AdapterMonth
import com.example.krew.adapter.GroupRVAdapter
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.DayInfoBinding
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem
import com.example.krew.model.Invitation
import com.example.krew.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var monthListAdapter: AdapterMonth
    lateinit var groupRVAdapter: GroupRVAdapter
    val groupArr = ArrayList<GroupItem>()
    lateinit var cur_user : User
    lateinit var dayInfoBinding: DayInfoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dayInfoBinding = DayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.Main).launch {
            ApplicationClass.updateCalendarList()
        }

        cur_user = ApplicationClass.cur_user
        initCalendar()
        initDrawer()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Firebase Communication", "Fetching FCM registration token failed ${task.exception}")
                return@OnCompleteListener
            }
            val deviceToken = task.result
            Log.d("Firebase Communication", "token=${deviceToken}")
        })

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Invitation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("Invitation Communication", "Invitation num on FB + ${dataSnapshot.children.count()}")
                for (inviteSnapshot in dataSnapshot.children) {
                    val invitation = inviteSnapshot.getValue(Invitation::class.java)
                    val key = inviteSnapshot.key
                    if(invitation!!.target == ApplicationClass.user_id) {
                        Log.e("Invitation Communication", "entering checkinvitation")
                        val intent = Intent(this@MainActivity, InviteActivity::class.java)
                        intent.putExtra("invite", invitation)
                        intent.putExtra("key", key)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase Communication", error.toString())
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val database = Firebase.database.getReference("Calendar")
        database.get().addOnSuccessListener {
            ApplicationClass.cur_calendar_list.clear()
            val iter = it.children.iterator()

            val calendar_list = ArrayList<Calendar>()
            while (iter.hasNext()) {
                calendar_list.add(iter.next().getValue<Calendar>() as Calendar)
            }
            for (cal in calendar_list) {
                if (cal.admin == ApplicationClass.user_id) {
                    ApplicationClass.cur_calendar_list.add(cal)
                    continue
                }
                if (cal.Participant != null) {
                    if (ApplicationClass.user_id in cal.Participant as ArrayList<String>) {
                        ApplicationClass.cur_calendar_list.add(cal)
                        continue
                    }
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                ApplicationClass.updateCalendarList()
                monthListAdapter.notifyDataSetChanged()
            }
            groupRVAdapter.notifyDataSetChanged()
        }
    }

    fun initCalendar() {
        //메인 캘린더 open
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        monthListAdapter = AdapterMonth()

        binding.mainTitle.text = cur_user.name + "의 달력"
        binding.calendarCustom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }

        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calendarCustom)

        val dayText = dayInfoBinding.dayText

        binding.mainBtnNav.setOnClickListener {
            if(!binding.drawer.isDrawerOpen(GravityCompat.START)){
                binding.drawer.openDrawer(GravityCompat.START)
            }else{
                binding.drawer.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun initDrawer() {
        val rv_nav = findViewById<RecyclerView>(R.id.rv_groups)!!
        val button = findViewById<ImageButton>(R.id.iv_add_groups)!!

        val tv_name = findViewById<TextView>(R.id.tv_name)
        val tv_email = findViewById<TextView>(R.id.tv_email)
        tv_name.text = cur_user.name
        tv_email.text = ApplicationClass.sSharedPreferences.getString("user_email", "").toString()

        button.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }

        groupRVAdapter = GroupRVAdapter(ApplicationClass.cur_calendar_list)
        rv_nav.adapter = groupRVAdapter
        rv_nav.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        groupRVAdapter.itemClickListener = object:GroupRVAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                if(ApplicationClass.cur_calendar_list[position].admin
                    == ApplicationClass.sSharedPreferences.getString("user_email", "").toString()){
                    val intent = Intent(this@MainActivity, GroupActivity::class.java)
                    intent.putExtra("id", ApplicationClass.cur_calendar_list[position].calendar_id)
                    startActivity(intent)
                }
            }

            override fun OnSwitchClick(position: Int, isChecked: Boolean) {
                val temp = ApplicationClass.cur_calendar_list[position]
                if (isChecked == true) {
                    if (temp !in ApplicationClass.on_calendar_list) {
                        ApplicationClass.on_calendar_list.add(temp)
                        monthListAdapter.notifyDataSetChanged()
                    }
                } else {
                    ApplicationClass.on_calendar_list.remove(temp)
                    monthListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun checkInvitation(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Invitation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("Invitation Communication", "Invitation num on FB + ${dataSnapshot.children.count()}")
                for (inviteSnapshot in dataSnapshot.children) {
                    val invitation = inviteSnapshot.getValue(Invitation::class.java)
                    val key = inviteSnapshot.key
                    if(invitation!!.target == ApplicationClass.user_id) {
                        Log.e("Invitation Communication", "entering checkinvitation")
                        val intent = Intent(this@MainActivity, InviteActivity::class.java)
                        intent.putExtra("invite", invitation)
                        intent.putExtra("key", key)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase Communication", error.toString())
            }
        })
    }
}