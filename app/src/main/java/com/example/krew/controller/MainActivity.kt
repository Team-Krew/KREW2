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
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.adapter.AdapterDay
import com.example.krew.adapter.AdapterDayInfo
import com.example.krew.adapter.AdapterMonth
import com.example.krew.adapter.GroupRVAdapter
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.DayInfoBinding
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem
import com.example.krew.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var groupRVAdapter: GroupRVAdapter
    val groupArr = ArrayList<GroupItem>()
    lateinit var cur_user : User
    lateinit var dayInfoBinding: DayInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dayInfoBinding = DayInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            cur_user = bundle.getSerializable("cur_user", User::class.java) as User
        }else{
            cur_user = bundle.getSerializable("cur_user") as User
        }
        initCalendar()
        initDrawer()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Firebase Communication", "Fetching FCM registration token failed ${task.exception}")
                return@OnCompleteListener
            }
            val deviceToken = task.result
            Log.e("Firebase Communication", "token=${deviceToken}")
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val mDatabase = Firebase.database.getReference("Calendar")
        val calendars =
            ApplicationClass.sSharedPreferences.getString("calendars", null)?.split(",")
        Log.e("Firebase communication", "${calendars?.size}")
        if (calendars != null) {
            for (id in calendars) {
                mDatabase.child(id).get().addOnSuccessListener {
                    val json = JSONObject(it.value.toString())
                    Log.e("Firebase communication",
                        "${json.getString("calendar_id")}," +
                                "${json.getString("name")}," +
                                "${json.getString("comment")}," +
                                "${json.getString("label")}")
                    groupArr.add(GroupItem(
                        json.getString("calendar_id"),
                        json.getString("name"),
                        json.getString("admin"),
                        resources.getColor(json.getInt("label"), null),
                        true
                    ))
                    groupRVAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        super.onPause()
        Log.e("Condition Check", "On Pause Called")
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

        groupRVAdapter = GroupRVAdapter(groupArr)
        rv_nav.adapter = groupRVAdapter
        rv_nav.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        groupRVAdapter.itemClickListener = object:GroupRVAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                if(groupArr[position].group_head == ApplicationClass.user_id){
                    val intent = Intent(this@MainActivity, GroupActivity::class.java)
                    intent.putExtra("id", groupArr[position].group_id)
                    startActivity(intent)

                }
            }

        }

    }


}