package com.example.krew.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.krew.databinding.ActivityFindLocationBinding

class FindLocation : AppCompatActivity() {
    lateinit var binding:ActivityFindLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFindLocationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLayout()
    }

    fun initLayout(){
        binding.apply {

        }
    }
}