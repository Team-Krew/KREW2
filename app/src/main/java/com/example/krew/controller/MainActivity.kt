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
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var groupRVAdapter: GroupRVAdapter
    val groupArr = ArrayList<GroupItem>()
    lateinit var cur_user : User
    lateinit var dayInfoBinding: DayInfoBinding

    lateinit var invitationArr:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dayInfoBinding = DayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val mDatabase = Firebase.database.getReference("Calendar")

        val calendars =
            ApplicationClass.sSharedPreferences.getString("calendars", null)?.split(",")
        Log.d("Firebase communication", "${calendars?.size}")


        ApplicationClass.updateCalendarList()

//        if (calendars != null) {
//            for (id in calendars) {
//                mDatabase.child(id).get().addOnSuccessListener {
//                    val cal: Calendar
//                    cal = it.getValue<Calendar>() as Calendar
//
//                    groupArr.add(GroupItem(
//                        cal.calendar_id,
//                        cal.name,
//                        cal.admin.toString(),
//                        resources.getColor(cal.label, null),
//                        true
//                    ))
//                    groupRVAdapter.notifyDataSetChanged()
//                }
//            }
//        }

        checkInvitation()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        super.onPause()
        groupArr.clear()
        groupRVAdapter.notifyDataSetChanged()
    }

    fun initCalendar() {
        //메인 캘린더 open
        val monthListManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth()

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
                if(groupArr[position].group_head
                    == ApplicationClass.sSharedPreferences.getString("user_email", "").toString()){
                    val intent = Intent(this@MainActivity, GroupActivity::class.java)
                    intent.putExtra("id", groupArr[position].group_id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun checkInvitation(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Invitation")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (inviteSnapshot in dataSnapshot.children) {
                    val invitation = inviteSnapshot.getValue(Invitation::class.java)
                    val key = inviteSnapshot.key
                    if(invitation!!.target == ApplicationClass.user_id) {
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