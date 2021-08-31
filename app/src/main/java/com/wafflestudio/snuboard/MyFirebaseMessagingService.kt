package com.wafflestudio.snuboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wafflestudio.snuboard.data.repository.NoticeNotiRepository
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.usecase.FCMTopicUseCase
import com.wafflestudio.snuboard.domain.usecase.NotifyUseCase
import com.wafflestudio.snuboard.presentation.MainActivity
import com.wafflestudio.snuboard.presentation.notice.NoticeDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var noticeNotiRepository: NoticeNotiRepository

    @Inject
    lateinit var notifyUseCase: NotifyUseCase

    @Inject
    lateinit var fcmTopicUseCase: FCMTopicUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
            remoteMessage.data.apply {
                val notificationInfo = mapOf(
                        "title" to get("title").toString(),
                        "body" to get("body").toString(),
                )
                val noticeId = get("noticeId")?.toInt()
                val preview = get("preview").toString()
                val departmentName = get("departmentName").toString()
                val departmentId = get("departmentId")?.toInt()
                val tags = get("tags").toString()

                if (noticeId != null && departmentId != null) {
                    if (noticeNotiRepository.getIsNotificationActiveWithDepartment(departmentId)) {
                        notificationInfo["body"]?.let {
                            notifyUseCase.addNoticeNoti(
                                    noticeId,
                                    it,
                                    departmentId,
                                    departmentName,
                                    preview,
                                    tags
                            ) { sendNotification(notificationInfo, noticeId, preview) }
                                    .subscribe({}, { e ->
                                        Timber.e(e)
                                    })
                        }
                    }
                }
            }
        } else {
            // 메시지 유형이 알림 메시지일 경우
            // Check if message contains a notification payload.
            // Set FCM title, body to android notification
            val notificationInfo: Map<String, String>
            remoteMessage.notification?.let {
                notificationInfo = mapOf(
                        "title" to it.title.toString(),
                        "body" to it.body.toString(),
                )
                sendNotification(notificationInfo, null, null)
            }
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        // 이 메소드가 호출되었을 때 실행해야 할 작업이 존재하면 여기에 작성하기
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        /** 변경된 토큰 가져오기 및 확인하기 */
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            pref.getString(SharedPreferenceConst.ACCESS_TOKEN_KEY, null)
                    ?.run {
                        Timber.d("Subscribing based on new FCM Token")
                        fcmTopicUseCase.subscribeAll(this)
                                .subscribe()
                    }
            // Get new FCM registration token
            val token = task.result
            Timber.d("newFCMToken: ${token.toString()}")
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun sendNotification(
            messageBody: Map<String, String>,
            noticeId: Int?,
            bigText: String?
    ) {
        val intent = if (noticeId == null) Intent(this, MyApplication::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        else arrayOf(
                MainActivity.intent(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                NoticeDetailActivity.intent(this, noticeId)
        )
        val androidNotiId = SystemClock.uptimeMillis().toInt()
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (noticeId == null)
                PendingIntent.getActivity(
                        this, androidNotiId, intent as Intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
            else
                PendingIntent.getActivities(
                        this, androidNotiId, intent as Array<Intent>,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
        } else {
            if (noticeId == null)
                PendingIntent.getActivity(
                        this, androidNotiId, intent as Intent,
                        PendingIntent.FLAG_ONE_SHOT // only mutable pendingIntent possible before API 23
                )
            else
                PendingIntent.getActivities(
                        this, androidNotiId, intent as Array<Intent>,
                        PendingIntent.FLAG_ONE_SHOT
                )
        }

        val channelId = if (noticeId == null) getString(R.string.default_notification_channel_id)
        else getString(R.string.notice_notification_channel_id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // icon, color는 메타 데이터에서 설정한 것으로 설정해주면 된다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_noti_trim)
                .setContentTitle(messageBody["title"])
                .setContentText(messageBody["body"])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        if (bigText != null) notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle().bigText(messageBody["body"] + '\n' + bigText)
        )

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    if (noticeId == null) "필수 알림" else "신규 게시글 알림",
                    if (noticeId == null) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(androidNotiId, notificationBuilder.build())
    }


}
