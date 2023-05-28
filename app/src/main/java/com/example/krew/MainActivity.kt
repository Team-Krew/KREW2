package com.example.krew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.GroupNavigationDrawerBinding
import com.example.krew.group.GroupActivity
import com.example.krew.group.GroupItem
import com.example.krew.group.GroupRVAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var groupRVAdapter:GroupRVAdapter

    val groupArr = ArrayList<GroupItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initDrawer()
    }

    fun init(){

        binding.btn.setOnClickListener{
            if(!binding.drawer.isDrawerOpen(GravityCompat.START)){
                binding.drawer.openDrawer(GravityCompat.START)
            } else{
                binding.drawer.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun initDrawer(){

        val button = findViewById<ImageButton>(R.id.iv_add_groups)!!
        val rv_nav = findViewById<RecyclerView>(R.id.rv_groups)!!

        button.setOnClickListener{
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }

        for(i in 1..5){
            groupArr.add(
                GroupItem(
                    i.toString(),
                    i.toString(),
                    false
                )
            )
        }
        groupRVAdapter = GroupRVAdapter(groupArr)
        rv_nav.adapter = groupRVAdapter
        rv_nav.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)




    }


}