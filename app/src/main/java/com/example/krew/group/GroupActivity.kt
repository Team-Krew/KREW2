package com.example.krew.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krew.R
import com.example.krew.databinding.ActivityGroupBinding
import com.example.krew.databinding.GroupItemBinding
import java.lang.reflect.Member

class GroupActivity : AppCompatActivity() {
    lateinit var binding:ActivityGroupBinding
    lateinit var memberRVAdapter:MemberRVAdapter

    val itemArr = ArrayList<MemberItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        init()
    }

    private fun init(){

    }

    private fun initRecycler(){

        for(i in 1..10){
            itemArr.add(
                MemberItem(i.toString(), i.toString())
            )
        }
        memberRVAdapter = MemberRVAdapter(itemArr)
        binding.rvRecycler.adapter = memberRVAdapter
        binding.rvRecycler.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)



    }

}