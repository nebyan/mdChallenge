package com.example.metindogun_challenge.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.metindogun_challenge.R

class ForegroundService: Service() {

    val CHANNEL_ID = "location_update_channel"
    val NOTIFICATION_ID = 1255

    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        setNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun setNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(): Notification{

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Photo")
            .setContentText("Location Updates")
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)

        return builder.build()

    }

}