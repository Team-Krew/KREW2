package com.example.krew.apimodel

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.apimodel.Constant2.Companion.CHANNEL_ID
import com.example.krew.apimodel.Constant2.Companion.NOTIFICATION_ID
import com.example.krew.controller.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class MyFirebaseMessagingService:FirebaseMessagingService() {
    var timeInMills2:Long?=null
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
            }else{
                send1Notification(remoteMessage)
            }
        }
    }
    //여기 구조보고 덮어쓰고
    private fun send1Notification(remoteMessage: RemoteMessage) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "my_channel" // 알림 채널 이름
        val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        val title = remoteMessage.data["title"].toString()
        val body = remoteMessage.data["body"]?.split(",")
        val date = body?.get(0)?.split("-")
        val hour = body?.get(1)?.split(":")
        val cal = Calendar.getInstance()
        cal.set(date?.get(0)!!.toInt(),date?.get(1)!!.toInt(),date?.get(2)!!.toInt(),hour?.get(0)!!.toInt(),hour?.get(1)!!.toInt())
        Log.i("timeInMillis",cal.timeInMillis.toString())
        Constant2.ALARM_TIMER = cal.timeInMillis
        Log.i("remotemsgtitle",title)
        Log.i("remotemsgdata",body.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, // 채널의 아이디
                "채널 이름입니다.", // 채널의 이름
                NotificationManager.IMPORTANCE_HIGH
                /*
                1. IMPORTANCE_HIGH = 알림음이 울리고 헤드업 알림으로 표시
                2. IMPORTANCE_DEFAULT = 알림음 울림
                3. IMPORTANCE_LOW = 알림음 없음
                4. IMPORTANCE_MIN = 알림음 없고 상태줄 표시 X
                 */
            )
            notificationChannel.enableVibration(true) // 진동 여부
            notificationChannel.description = "채널의 상세정보입니다." // 채널 정보
            notificationManager.createNotificationChannel(
                notificationChannel)
        }



        val builder = NotificationCompat.Builder(this@MyFirebaseMessagingService, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_group_24)
            .setContentTitle(title) // 제목
            .setContentText(body.toString()) // 내용
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val triggerTime = SystemClock.elapsedRealtime()+ cal.timeInMillis
        val contentIntent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this@MyFirebaseMessagingService,
            NOTIFICATION_ID, // requestCode
            contentIntent, // 알림 클릭 시 이동할 인텐트
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            /*
            1. FLAG_UPDATE_CURRENT : 현재 PendingIntent를 유지하고, 대신 인텐트의 extra data는 새로 전달된 Intent로 교체
            2. FLAG_CANCEL_CURRENT : 현재 인텐트가 이미 등록되어있다면 삭제, 다시 등록
            3. FLAG_NO_CREATE : 이미 등록된 인텐트가 있다면, null
            4. FLAG_ONE_SHOT : 한번 사용되면, 그 다음에 다시 사용하지 않음
             */
        )
        Log.i("caltimeInMillis",cal.timeInMillis.toString())
        notificationManager.notify(NOTIFICATION_ID, builder.build())


//        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
//        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("invitation", "1")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP //앱을 top으로 올림
//
//        val resultPendingIntent = PendingIntent.getActivity(this, 100,
//            intent, PendingIntent.FLAG_IMMUTABLE)
//
//        // 알림에 대한 UI 정보, 작업
//        Log.i("title",remoteMessage.data["title"].toString())
//        Log.i("body",remoteMessage.data["body"].toString())
//        Log.i("System.currenttimeMillis()",System.currentTimeMillis().toString())
//        val date = body?.get(0)?.split("-")
//        val hour = body?.get(1)?.split(":")
//        val cal = Calendar.getInstance()
//        cal.set(date?.get(0)!!.toInt(),date?.get(1)!!.toInt(),date?.get(2)!!.toInt(),hour?.get(0)!!.toInt(),hour?.get(1)!!.toInt())
//        val timeInMills = cal.timeInMillis
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.baseline_location_on_24)                     // 아이콘 설정
//            .setContentTitle(remoteMessage.data["title"].toString())// 제목
//            .setContentText(remoteMessage.data["body"].toString())  // 메시지 내용
//            .setAutoCancel(true) // 알람클릭시 삭제여부
//            .setContentIntent(resultPendingIntent)
//            .setWhen(timeInMills)
//
//        // 알림 생성
//        notificationManager.notify(uniId, notificationBuilder.build())
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.e(TAG, "Notification Called")
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        Log.i("checkmail","checknotification")
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
class Constant2{
    companion object{
        val NOTIFICATION_ID = 0
        val CHANNEL_ID = "notification_channel"
        //알림시간 설정 (여기다가 계산한 시간값을 넣어야 하고)

        var ALARM_TIMER :Long?=null
    }
}