package com.example.krew.apimodel

import android.util.Log
import com.example.krew.ApplicationClass
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ScheduleMessageService : FirebaseMessagingService() {

    val TAG:String = "FCM Service"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "new Token: $token")
        Log.i("로그: ", "성공적으로 토큰을 저장함")
        // 토큰 값을 따로 저장
        ApplicationClass.spEditor.putString("token", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val body = remoteMessage.data["body"]
        val title = remoteMessage.data["title"]


        Log.e(TAG, "remote data: ${remoteMessage.data}")
        Log.e(TAG, "remote notification: ${remoteMessage.notification}")

        if(remoteMessage.data.isNotEmpty()){
            Log.e(TAG, "remote data: ${remoteMessage.data["title"]}")
            Log.e(TAG, "remote data: ${remoteMessage.data["body"]}")
            if(remoteMessage.data["title"] == "초대합니다."){

            } else{

            }
        }
    }

}