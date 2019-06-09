package maderski.chargingindicator.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

/**
 * Created by Jason on 6/17/17.
 */

object ServiceUtils {
    const val FOREGROUND_NOTIFICATION_ID = 3449

    fun startService(context: Context, serviceClass: Class<*>, tag: String) {
        val intent = Intent(context, serviceClass)
        intent.addCategory(tag)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(intent)
        } else {
            context.startForegroundService(intent)
        }
    }

    fun startBindedService(context: Context, serviceClass: Class<*>, tag: String, serviceConnection: ServiceConnection) {
        val intent = Intent(context, serviceClass)
        intent.addCategory(tag)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(intent)
        } else {
            context.startForegroundService(intent)
        }
    }

    fun stopService(context: Context, serviceClass: Class<*>, tag: String) {
        try {
            val intent = Intent(context, serviceClass)
            intent.addCategory(tag)
            context.stopService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopBindedService(context: Context, serviceClass: Class<*>, tag: String, serviceConnection: ServiceConnection) {
        try {
            val intent = Intent(context, serviceClass)
            intent.addCategory(tag)
            context.unbindService(serviceConnection)
            context.stopService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)

        return services.any { runningServiceInfo ->
            runningServiceInfo.service.className == serviceClass.name
        }
    }

    fun createServiceNotification(id: Int,
                                  title: String,
                                  message: String,
                                  service: Service,
                                  channelId: String,
                                  channelName: String,
                                  @DrawableRes icon: Int,
                                  isOngoing: Boolean) {

        val notification = getNotification(title, message, service, channelId, channelName, icon, isOngoing)
        service.startForeground(id, notification)
    }

    fun updateServiceNotification(id: Int,
                                  title: String,
                                  message: String,
                                  context: Context,
                                  channelId: String,
                                  channelName: String,
                                  @DrawableRes icon: Int,
                                  isOngoing: Boolean) {

        val notification = getNotification(title, message, context, channelId, channelName, icon, isOngoing)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }

    private fun getNotification(title: String,
                                message: String,
                                context: Context,
                                channelId: String,
                                channelName: String,
                                @DrawableRes icon: Int,
                                isOngoing: Boolean): Notification {
        val builder: NotificationCompat.Builder

        builder = if (Build.VERSION.SDK_INT < 26) {
            NotificationCompat.Builder(context, channelId)
        } else {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = getNotificationChannel(channelId, channelName)
            notificationManager.createNotificationChannel(channel)

            NotificationCompat.Builder(context, channelId)
        }

        val notification = builder
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setOnlyAlertOnce(true)
                .build()

        if (isOngoing) {
            notification.flags = NotificationCompat.FLAG_FOREGROUND_SERVICE
        }

        return notification
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(channelId: String, channelName: String): NotificationChannel {
        val notificationChannel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_MIN)
        notificationChannel.enableVibration(false)
        notificationChannel.setShowBadge(false)
        return notificationChannel
    }
}
