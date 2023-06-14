package com.example.krew.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.krew.R
import com.example.krew.databinding.ActivityCheckRegisterBinding

class CheckRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){
        binding.apply {
            checkOk.setOnClickListener {
                val intent = Intent(this@CheckRegisterActivity,DayInfoActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}