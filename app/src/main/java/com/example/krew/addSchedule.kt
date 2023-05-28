package com.example.krew

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.krew.databinding.ActivityAddScheduleBinding

class addSchedule : AppCompatActivity() {
    lateinit var binding:ActivityAddScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initLayout()
    }
    fun initLayout(){
        binding.apply {
            backBtn.setOnClickListener {
                val intent = Intent(this@addSchedule,MainActivity::class.java)
                startActivity(intent)
            }
            checkBtn.setOnClickListener{
                //해당 부분에 main calendar View로 돌아가는 intent문 작성
                //일정 db에 업데이트하는 문 추가 작성
            }
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    Toast.makeText(this@addSchedule,"check됨",Toast.LENGTH_SHORT).show()
                    //시간 나옴
                }else{
                    Toast.makeText(this@addSchedule,"check안됨",Toast.LENGTH_SHORT).show()
                    //시간 안나옴
                }
            }
        }
    }
}