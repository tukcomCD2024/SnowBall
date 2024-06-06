package com.snowball.memetory.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.snowball.memetory.R
import com.snowball.memetory.presentation.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

        // 토큰 저장
        TokenManager.saveFcmToken(token)
        // 토큰 전송 상태 초기화
        TokenManager.setTokenSentToServer(false)

    }

    // Foreground에서 Push Service를 받기 위해 Notification 설정
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
//        Log.d(TAG, "From: $remoteMessage")
//        Log.d(TAG, "From: ${remoteMessage.data}")
//        Log.d(TAG, "From: ${remoteMessage.from}")
//        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
//        }

        Log.d(TAG, "FCMToken: ${TokenManager.getFCMToken()}")
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        remoteMessage.notification?.let {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d(TAG, "Message Notification Title: ${it.title}")
                Log.d(TAG, "Message Notification Body: ${it.body}")
                sendNotification(it.title!!, it.body!!)
            }
        }
    }
    suspend fun getFirebaseToken(): String {
        // withContext를 사용하여 IO 스레드에서 실행
        return withContext(Dispatchers.IO) {
            try {
                // Firebase 토큰 비동기 요청을 await()으로 기다립니다.
                FirebaseMessaging.getInstance().token.await()
            } catch (e: Exception) {
                Log.e("FCM", "Failed to get Firebase token", e)
                ""
            }
        }
    }

    private suspend fun sendNotification(title: String, body: String) = withContext(Dispatchers.Main) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = System.currentTimeMillis().toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID = "memetory_notifications"
            val channelName = "Memetory Notifications"
            val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

//            val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java).apply{
//                putExtra("destination", "LockerFragment")
////                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            }
//            val pendingIntent = PendingIntent.getActivity(this@MyFirebaseMessagingService, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//            val notificationBuilder = NotificationCompat.Builder(this@MyFirebaseMessagingService, channelID)
//                .setSmallIcon(R.drawable.main_logo)  // ensure you have this icon
//                .setContentTitle(title)
//                .setContentText(body)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
            // NavDeepLinkBuilder를 사용하여 PendingIntent 생성
            val pendingIntent = NavDeepLinkBuilder(this@MyFirebaseMessagingService)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.nav_main_bottom_graph)
                .setDestination(R.id.lockerFragment)
                .createPendingIntent()

            val notificationBuilder = NotificationCompat.Builder(this@MyFirebaseMessagingService, channelID)
                .setSmallIcon(R.drawable.main_logo)  // 아이콘 설정 확인
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(notificationID, notificationBuilder.build())
            Toast.makeText(applicationContext, "$body", Toast.LENGTH_LONG).show()
        }
    }
}