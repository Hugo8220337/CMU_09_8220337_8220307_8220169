package ipp.estg.cmu_09_8220169_8220307_8220337.services.workers

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.LocalDatabase
import ipp.estg.cmu_09_8220169_8220307_8220337.repositories.DailyTasksRepository
import ipp.estg.cmu_09_8220169_8220307_8220337.services.DailyRemeinderService

class DailyReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val dailyTasksRepo: DailyTasksRepository

    init {
        // inicializa o repositório das tarefas diárias
        val dbDao = LocalDatabase.getDatabase(context).dailyTaskCompletionDao
        dailyTasksRepo = DailyTasksRepository(dbDao)

    }

    override fun doWork(): Result {
        checkExercisesAndNotify()
        return Result.success()
    }

    private fun checkExercisesAndNotify() {
//        val calendar = Calendar.getInstance()

        val areTodayTasksDone = dailyTasksRepo.areTodaysTasksDone()

        if (!areTodayTasksDone) {
            showExerciseReminder()
        }
    }

    private fun showExerciseReminder() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(
            context,
            DailyRemeinderService.TIMER_SERVICE_NOTIICATION_CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher_75hard_challange_logo_foreground)
            .setContentTitle("Exercícios Pendentes!")
            .setContentText("Não se esqueça de completar seus exercícios hoje!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(EXERCISE_REMINDER_NOTIFICATION_ID, notification)
    }

    companion object {
        private const val EXERCISE_REMINDER_NOTIFICATION_ID = 2
    }
}