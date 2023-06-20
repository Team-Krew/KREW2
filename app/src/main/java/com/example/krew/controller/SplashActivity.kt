package com.example.krew.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.krew.ApplicationClass
import com.example.krew.ApplicationClass.Companion.cur_user
import com.example.krew.R
import com.example.krew.model.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class SplashActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        addCalendar()
        val par_list = arrayListOf<String>("whereami2048@gmail.com")
//        val sch_list = arrayListOf<Schedule>(Schedule(0, arrayListOf<Calendar>()))
//        database.child("Calendar").child("0").setValue(Calendar("0", "whereami2048@gmail.com", "good gag",
//            2131034164, "whereami2048@gmail.com", par_list, null))
        Handler(Looper.getMainLooper()).postDelayed({
            if(ApplicationClass.sSharedPreferences.getString("user_uid", "") != ""){
                val cur_uid = ApplicationClass.sSharedPreferences.getString("user_uid", "")!!
                database.child("User").child(cur_uid).get().addOnSuccessListener {
                    val intent = Intent(this, MainActivity::class.java)
                    val cur_user2 = it.getValue<User>() as User
                    cur_user = cur_user2
                    intent.putExtra("cur_user", cur_user2)
                    startActivity(intent)
                    finish()
                }

            }else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)
    }

//    fun addUser(){
//        val tempUser = User(FirebaseAuth.getInstance().currentUser!!.uid, "김용현", "test_address", "test_comment", "test_img", "test_time")
//
//        database.child("User").child(tempUser.user_id).setValue(tempUser)
//    }
//
//    fun addCalendar(){
//        val list = ArrayList<User>()
//        list.add(User("user_id", "김용현", "test_address", "test_comment", "test_img", "test_time"))
//        list.add(User("user_id", "이석준", "test_address", "test_comment", "test_img", "test_time"))
//        list.add(User("user_id", "주용은", "test_address", "test_comment", "test_img", "test_time"))
//        list.add(User("user_id", "강건우", "test_address", "test_comment", "test_img", "test_time"))
//
//        val tempCalendar = Calendar("id", "test1", "test_comment",
//            "test_label", User(FirebaseAuth.getInstance().currentUser!!.uid, "김용현", "test_address", "test_comment", "test_img", "test_time"),
//            list
//        )
//        database.child("Calendar").child(tempCalendar.name).setValue(tempCalendar)
//    }
//
//    fun addSchedule(){
//
//    }
}