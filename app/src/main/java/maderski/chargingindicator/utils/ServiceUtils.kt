package maderski.chargingindicator.utils

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.NotificationChannel
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat

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

    fun stopService(context: Context, serviceClass: Class<*>, tag: String) {
        try {
            val intent = Intent(context, serviceClass)
            intent.addCategory(tag)
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
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun scheduleJob(context: Context, jobServiceClass: Class<*>) {
        val jobServiceComponent = ComponentName(context, jobServiceClass)
        val builder = JobInfo.Builder(0, jobServiceComponent)
        builder.setMinimumLatency(1000)
        builder.setOverrideDeadline(10000)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    fun isJobScheduled(context: Context): Boolean {
        var isScheduled = false
        // Get Job Scheduler
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfos = jobScheduler.allPendingJobs
        for (jobInfo in jobInfos) {
            if (jobInfo.id == 0) {
                isScheduled = true
                break
            }
        }

        return isScheduled
    }
}
