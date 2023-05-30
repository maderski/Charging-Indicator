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
        context.startForegroundService(intent)
    }

    fun startBindedService(context: Context, serviceClass: Class<*>, tag: String, serviceConnection: ServiceConnection) {
        val intent = Intent(context, serviceClass)
        intent.addCategory(tag)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        context.startForegroundService(intent)
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

        val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(service, notificationManager, title, message, channelId, channelName, icon, isOngoing)
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
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = getNotification(context, notificationManager, title, message, channelId, channelName, icon, isOngoing)
        notificationManager.notify(id, notification)
    }

    private fun getNotification(context: Context,
                                notificationManager: NotificationManager,
                                title: String,
                                message: String,
                                channelId: String,
                                channelName: String,
                                @DrawableRes icon: Int,
                                isOngoing: Boolean): Notification {

        val channel = getNotificationChannel(channelId, channelName)
        notificationManager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(context, channelId)

        with(builder) {
            setSmallIcon(icon)
            setContentTitle(title)
            setContentText(message)
            setOnlyAlertOnce(true)
        }

        val notification = builder.build()

        notification.flags = if (isOngoing) {
            NotificationCompat.FLAG_FOREGROUND_SERVICE
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        return notification
    }

    private fun getNotificationChannel(channelId: String, channelName: String): NotificationChannel {
        val notificationChannel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_MIN)
        notificationChannel.enableVibration(false)
        notificationChannel.setShowBadge(false)
        return notificationChannel
    }
}
