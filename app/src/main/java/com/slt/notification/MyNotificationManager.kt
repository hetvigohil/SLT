package com.slt.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.slt.R
import com.slt.notification.FirebaseService.Companion.CHANNEL_ID
import com.slt.notification.FirebaseService.Companion.megapoint_group


class MyNotificationManager(
    mCtx: Context

) {
    var ID_SMALL_NOTIFICATION = 235
    private val SUMMARY_ID = 245

    private var mCtx: Context? = null
    private var notificationManager: NotificationManager? = null
    var NewNotification = "NewNotification"

    init {
        this.mCtx = mCtx;
        notificationManager =
            mCtx?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    var v = longArrayOf(500, 1000)

    fun showSmallNotification(
        title: String,
        message: String,
        ContentTitle: String?,
        intent: Intent?
    ) {
        val pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, 0)
        val newMessageNotification2: Notification = NotificationCompat.Builder(this!!.mCtx!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setColor(mCtx!!.resources.getColor(R.color.colorTheme))
            .setContentTitle(title)
            .setContentText(message)
            .setGroup(megapoint_group)
            .setChannelId(CHANNEL_ID)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .build()
        val summaryNotification: Notification = NotificationCompat.Builder(mCtx!!, CHANNEL_ID)
            .setContentTitle(ContentTitle) //set content text to support devices running API level < 24
            .setContentText(ContentTitle)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(
                mCtx!!.resources.getColor(R.color.colorTheme)
            ) //build summary info into InboxStyle template
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("$title - $message")
                    .setBigContentTitle(title)
                    .setSummaryText(ContentTitle)
            ) //specify which group this notification belongs to
            .setGroup(megapoint_group)
            .setChannelId(CHANNEL_ID)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true) //set this notification as the summary for the group
            .setGroupSummary(true)
            .build()
        notificationManager!!.notify(System.currentTimeMillis().toInt(), newMessageNotification2)
        notificationManager!!.notify(System.currentTimeMillis().toInt(), summaryNotification)
    }

}