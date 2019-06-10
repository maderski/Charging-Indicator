package maderski.chargingindicator.helpers.permission

import android.app.Activity
import android.content.Context

interface PermissionHelper {
    fun checkToLaunchSystemOverlaySettings(activity: Activity)
    fun hasOverlayPermission(context: Context): Boolean
    fun launchSystemOverlayPermissionSettings(activity: Activity)
}