package com.example.krew.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.adapter.AdapterMonth
import com.example.krew.adapter.GroupRVAdapter
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.DayInfoBinding
import com.example.krew.model.Calendar
import com.example.krew.model.User

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

    private fun initDrawer(){
        val button = findViewById<ImageButton>(R.id.iv_add_groups)!!
        val rv_nav = findViewById<RecyclerView>(R.id.rv_groups)!!
        val tv_name = findViewById<TextView>(R.id.tv_name)
        val tv_email = findViewById<TextView>(R.id.tv_email)
        tv_name.text = cur_user.name
        tv_email.text = ApplicationClass.sSharedPreferences.getString("user_email", "").toString()
        button.setOnClickListener{
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }

        groupRVAdapter = GroupRVAdapter(groupArr)
        rv_nav.adapter = groupRVAdapter
        rv_nav.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)

    }


}