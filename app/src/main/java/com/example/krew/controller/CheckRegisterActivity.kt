package com.example.krew.controller

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.apimodel.Constant2.Companion.ALARM_TIMER
import com.example.krew.apimodel.Constant2.Companion.NOTIFICATION_ID
import com.example.krew.apimodel.MyFirebaseMessagingService
import com.example.krew.databinding.ActivityCheckRegisterBinding
import com.example.krew.model.Schedule
import com.example.krew.model.TempUser
import com.example.krew.model.Time
import com.example.krew.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import java.net.HttpURLConnection
import java.net.URL

class CheckRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var apikey = getString(R.string.apiKey)
        super.onCreate(savedInstanceState)
        binding = ActivityCheckRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //최단경로 받아오는 코드
        //origin에 사용자의 출발지 입력
        //destination에 스케줄의 장소 입력
//        val origin = "의명유치원" // 출발지 좌표 설정
//        val destination = "건국대학교 공과대학" // 도착지 좌표 설정
//
//        val geoApiContext = GeoApiContext.Builder()
//            .apiKey(apikey)
//            .build()
//
//        val directionsApiRequest = DirectionsApi.newRequest(geoApiContext)
//            .origin(origin)
//            .destination(destination)
//            .mode(TravelMode.TRANSIT) // 이동 수단 설정
//
//        val result: DirectionsResult = directionsApiRequest.await()
//        if (result.routes != null && result.routes.isNotEmpty()) {
//            val route = result.routes[0]
//            if (route.legs != null && route.legs.isNotEmpty()) {
//                val leg = route.legs[0]
//                Log.i("leg",leg.toString())
//                val duration = leg.duration.toString()
//                Log.i("duration",duration)
//                // 최단 경로 소요 시간 사용
//            }
//        }

        init()
    }

    fun init(){
         // set : 일회성 알림
        binding.apply {
            checkOk.setOnClickListener {
                val intent = Intent(this@CheckRegisterActivity,DayInfoActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@CheckRegisterActivity,MyFirebaseMessagingService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        Log.i("ALARM_TIMER2", ALARM_TIMER.toString())
        val triggerTime = ALARM_TIMER // ms 이기 때문에 초단위로 변환 (*1000)
        if (triggerTime != null) {
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

}