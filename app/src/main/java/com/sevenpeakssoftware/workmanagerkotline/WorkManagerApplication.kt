package com.sevenpeakssoftware.workmanagerkotline

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class WorkManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val chanal = NotificationChannel(
                "download_chanel",
                "File Download...",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanal)
        }
    }
}