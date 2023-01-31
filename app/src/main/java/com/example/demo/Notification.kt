package com.example.demo


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.demo.activity.MainActivity

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "title extra"
const val messageExtra = "message extra"


class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.map_icon)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(intent.getStringExtra(messageExtra)))
            .setContentIntent(
                PendingIntent.getActivity(
                context, // Context from onReceive method.
                0,
                Intent(context, MainActivity::class.java), // Activity you want to launch onClick.
                0
                )
            )
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}