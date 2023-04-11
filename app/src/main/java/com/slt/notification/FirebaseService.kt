package com.slt.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.slt.R
import com.slt.data.datamanager.DataManager
import com.slt.data.preferences.PreferenceManager
import com.slt.extensions.showLog
import com.slt.extra.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class FirebaseService : FirebaseMessagingService() {

    val TAG = "FCM"

    companion object {
        var CHANNEL_ID = "fcm_default_channel_megapoint"
        var megapoint_group = "megapoint_group"
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        createNotificationChannel()
        if (remoteMessage.notification != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.notification!!.body)
        }
        // Check if message contains a data payload.
        // Check if message contains a data payload.
        if (remoteMessage.data != null) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
            try {
                sendPushNotification(remoteMessage.data)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = resources.getString(R.string.app_name)
            val description = "MegaPointClub"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.enableVibration(true)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun sendPushNotification(
        remoteMessage: MutableMap<String, String>
    ) {
        var conversionId = "0"
        val json = JSONObject(remoteMessage.toMap())

        val type = json.getString("type")
        if (type.equals( Constants.NOTIFY,ignoreCase = true)){
            val title = json.getString("title")
            val body = json.getString("message")
            val mNotificationManager =
                MyNotificationManager(applicationContext)

            /*val intent = Intent(applicationContext, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            mNotificationManager.showSmallNotification(
                title,
                body,
                "New Notification",
                intent
            )*/
        }else if (type.equals("logout", ignoreCase = true)) {
            CoroutineScope(Dispatchers.IO).launch {
                showLog("Logout push received")

                FirebaseMessaging.getInstance().unsubscribeFromTopic(
                    DataManager.getInstance().getPreference()
                        .getString(PreferenceManager.SUBSCRIPTION_TOPIC)
                )

                GlobalScope.launch {
                    DataManager.getInstance().getDatabase().clearAllTables()
                }
                DataManager.getInstance().getPreference().clearPreference()
            }
        }

    }

}