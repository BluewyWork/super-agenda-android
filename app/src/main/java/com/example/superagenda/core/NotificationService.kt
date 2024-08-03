package com.example.superagenda.core

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.superagenda.MainActivity
import com.example.superagenda.R

class NotificationService(
   private val context: Context,
) {
   private val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

   companion object {
      const val CHANNEL_ID = "channel_1"
   }

   fun showNotification(title: String, description: String) {
      val activityIntent = Intent(context, MainActivity::class.java)

      val activityPendingIntent = PendingIntent.getActivity(
         context,
         1,
         activityIntent,
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
      )

      val notification = NotificationCompat.Builder(context, CHANNEL_ID)
         // setting a notification icon is a must
         .setSmallIcon(R.drawable.ic_launcher_background)
         .setContentTitle(title)
         .setContentText(description)
         .setContentIntent(activityPendingIntent)
         .build()

      notificationManager.notify(1, notification)
   }
}