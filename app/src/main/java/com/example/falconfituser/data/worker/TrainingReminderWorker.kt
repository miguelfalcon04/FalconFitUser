package com.example.falconfituser.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.falconfituser.FalconFitUserApplication
import com.example.falconfituser.R

/**
 * Worker class responsible for displaying a training reminder notification.
 * This is scheduled using WorkManager to run in the background.
 *
 * @param context Application context
 * @param workerParams Parameters passed to the worker
 */
class TrainingReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "training_reminder_channel"
        const val NOTIFICATION_ID = 1
    }

    /**
     * Executes the background work to display the training reminder notification.
     *
     * @return Result of the work execution (success or failure)
     */
    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    /**
     * Builds and displays the training reminder notification.
     * Also ensures the notification channel is created.
     */
    private fun showNotification() {
        createNotificationChannel()

        val intent = Intent(applicationContext, FalconFitUserApplication::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_superset)
            .setContentTitle("¡Hora de entrenar!")
            .setContentText("No olvides tu entrenamiento diario para mantenerte en forma")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Creates the notification channel.
     * This channel is used to display training reminder notifications.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Recordatorios de entrenamiento"
            val descriptionText = "Canal para mostrar recordatorios de entrenamiento"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}