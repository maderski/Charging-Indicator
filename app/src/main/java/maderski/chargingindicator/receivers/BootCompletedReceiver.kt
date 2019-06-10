package maderski.chargingindicator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import maderski.chargingindicator.workers.OnBootWorker

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.action
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                // Create and add work request to work manager
                val workOnBootRequest = OneTimeWorkRequestBuilder<OnBootWorker>()
                        .addTag(OnBootWorker.TAG)
                        .build()
                WorkManager.getInstance().enqueue(workOnBootRequest)
            }
        }
    }
}