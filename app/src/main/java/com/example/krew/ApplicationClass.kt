package com.example.krew

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.util.Log


// 앱이 실행될때 1번만 실행이 됩니다.
class ApplicationClass : Application() {
    // 코틀린의 전역변수 문법
    companion object {
        // 만들어져있는 SharedPreferences 를 사용해야합니다. 재생성하지 않도록 유념해주세요
        lateinit var sSharedPreferences: SharedPreferences

        // JWT Token Header 키 값
        val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"


        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        //유저 아이디
        var user_id:String?=null
    }


    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()
        Log.e("created application", "wowowo")
        sSharedPreferences =
            applicationContext.getSharedPreferences("KREW", MODE_PRIVATE)

        sSharedPreferences.edit().putString("user_id", "seokjun2000@gmail.com").apply()
        user_id = sSharedPreferences.getString("user_id", null)
    }
}