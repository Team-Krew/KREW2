package com.example.krew.apimodel

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.controller.LoginActivity
import com.example.krew.controller.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {

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
                sendNotification(remoteMessage)
            } else{
                send1Notification(remoteMessage)
            }
        }
    }

    private fun send1Notification(remoteMessage: RemoteMessage) {
        val channelId = "my_channel" // 알림 채널 이름
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("invitation", "1")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //앱을 top으로 올림

        val resultPendingIntent = PendingIntent.getActivity(this, 100,
            intent, PendingIntent.FLAG_IMMUTABLE)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_location_on_24)                     // 아이콘 설정
            .setContentTitle(remoteMessage.data["title"].toString())// 제목
            .setContentText(remoteMessage.data["body"].toString())  // 메시지 내용
            .setAutoCancel(true)                                    // 알람클릭시 삭제여부
            .setContentIntent(resultPendingIntent)

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.e(TAG, "Notification Called")
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.baseline_group_24)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["body"])
            .setAutoCancel(true)

        /*notification 클릭 관련 처리*/
//        val intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra("invitation", "check")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //앱을 top으로 올림
        val intent = packageManager.getLaunchIntentForPackage(applicationContext.packageName)
        intent?.putExtra("invitation", "check")
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

//        val pendingIntent = PendingIntent.getActivity(this, 100,
//            intent, PendingIntent.FLAG_IMMUTABLE)
//        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }
}