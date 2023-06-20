package com.example.krew.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.krew.R
import com.example.krew.databinding.ActivityCheckRegisterBinding
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class CheckRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var apikey = getString(R.string.apiKey)
        super.onCreate(savedInstanceState)
        binding = ActivityCheckRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val origin = "의명유치원" // 출발지 좌표 설정
        val destination = "건국대학교 공과대학" // 도착지 좌표 설정

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apikey)
            .build()

        val directionsApiRequest = DirectionsApi.newRequest(geoApiContext)
            .origin(origin)
            .destination(destination)
            .mode(TravelMode.TRANSIT) // 이동 수단 설정

        val result: DirectionsResult = directionsApiRequest.await()
        if (result.routes != null && result.routes.isNotEmpty()) {
            val route = result.routes[0]
            if (route.legs != null && route.legs.isNotEmpty()) {
                val leg = route.legs[0]
                Log.i("leg",leg.toString())
                val duration = leg.duration.toString()
                Log.i("duration",duration)
                // 최단 경로 소요 시간 사용
            }
        }
        init()
    }

    fun init(){
        binding.apply {
            checkOk.setOnClickListener {
                val intent = Intent(this@CheckRegisterActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setalarm(){
        // 알람 정보 받아오기
        //schedule안의 admin, participant정보 받아오기.
        //admin, participant들이 User에 있는 사람인지 체크
        //있으면
    }
}