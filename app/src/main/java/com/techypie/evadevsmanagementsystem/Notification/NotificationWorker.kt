package com.techypie.evadevsmanagementsystem.Notification

import android.Manifest
import android.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mycompanny.stories.MainActivity
import com.mycompanny.stories.R
import com.techypie.evadevsmanagementsystem.Activities.ActivityAttendance

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {

        val intent = Intent(applicationContext, ActivityAttendance::class.java)

//        var mediaPlayer = MediaPlayer.create(applicationContext, R.raw.test)
//        mediaPlayer.start()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, "scheduled_channel")
            .setSmallIcon(R.drawable.btn_star)
            .setContentTitle("Mark Attedance")
            .setContentText("Please Mark Student Attendance")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext)
            .notify(101, builder.build())

        return Result.success()
    }
}