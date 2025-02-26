package com.example.falconfituser.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkManagerHelper {
    private const val TRAINING_REMINDER_WORK = "training_reminder_work"

    // Configurar el intervalo para las notificaciones (por ejemplo, cada 24 horas)
    fun setupTrainingReminders(context: Context, intervalHours: Long = 24) {
        // Crear una solicitud de trabajo periódico
        val trainingReminderRequest = PeriodicWorkRequestBuilder<TrainingReminderWorker>(
            intervalHours, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TRAINING_REMINDER_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            trainingReminderRequest
        )
    }

    // Cancelar los recordatorios de entrenamiento
    fun cancelTrainingReminders(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(TRAINING_REMINDER_WORK)
    }

    fun testNotificationNow(context: Context) {
        // Para pruebas inmediatas
        val testReminderRequest = OneTimeWorkRequestBuilder<TrainingReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "test_notification",
            ExistingWorkPolicy.REPLACE,
            testReminderRequest
        )
    }
}