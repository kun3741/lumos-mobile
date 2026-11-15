package ua.lumos.mobile

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

private const val ScheduleChannelId = "schedule_updates"
private const val PermissionTestNotificationId = 8

fun hasNotificationPermission(context: Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED

fun showNotificationPermissionConfirmed(context: Context) {
    if (!hasNotificationPermission(context)) return

    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(
        NotificationChannel(
            ScheduleChannelId,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
    )

    val notification = Notification.Builder(context, ScheduleChannelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(context.getString(R.string.notification_test_title))
        .setContentText(context.getString(R.string.notification_test_text))
        .setAutoCancel(true)
        .build()

    manager.notify(PermissionTestNotificationId, notification)
}
