package com.example.krew

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.example.krew.model.Calendar
import com.example.krew.model.GroupItem
import com.example.krew.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {
    // 코틀린의 전역변수 문법
    companion object {
        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var sSharedPreferences: SharedPreferences
        lateinit var spEditor: SharedPreferences.Editor

        // JWT Token Header 키 값
        val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"

        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // 현재 로그인 유저 User 객체
        lateinit var cur_user: User

        // 현재 유저가 속한 Calendar 리스트
        var cur_calendar_list: ArrayList<Calendar> = ArrayList<Calendar>()

        //유저 아이디
        var user_id: String? = null

        //유저 캘린더 리스트 업데이트 함수
        fun updateCalendarList() {
            val database = Firebase.database.getReference("Calendar")
            database.get().addOnSuccessListener {
                val iter = it.children.iterator()

                val calendar_list = ArrayList<Calendar>()
                val user_calendar_list = ArrayList<Calendar>()
                while (iter.hasNext()) {
                    calendar_list.add(iter.next().getValue<Calendar>() as Calendar)
                }
                for (cal in calendar_list) {
                    if (cal.admin == user_id) {
                        cur_calendar_list?.add(cal)
                        continue
                    }
                    if (cal.Participant != null) {
                        if (user_id in cal.Participant as ArrayList<String>) {
                            cur_calendar_list?.add(cal)
                            continue
                        }
                    }
                }
            }
        }
    }

        // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
        override fun onCreate() {
            super.onCreate()
            sSharedPreferences = applicationContext.getSharedPreferences("KREW", MODE_PRIVATE)
            spEditor = sSharedPreferences.edit()

            //spEditor.remove("calendars").apply()
            user_id = sSharedPreferences.getString("user_email", null)
        }

}