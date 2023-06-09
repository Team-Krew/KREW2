package com.example.krew.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.krew.R
import com.example.krew.model.Calendar
import com.example.krew.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {
//    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        addCalendar()
//        database.child("Calendar").orderByKey().addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (i in snapshot.children){
//                    println(i.child("admin"))
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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