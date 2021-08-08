package com.wafflestudio.snuboard.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.data.room.NoticeNotiDao
import com.wafflestudio.snuboard.di.SharedPreferenceConst.IS_NOTIFICATION_ACTIVE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject
import javax.inject.Singleton

interface NoticeNotiRepository {

    fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable

    fun deleteNoticeNoti(noticeNoti: NoticeNoti): Completable

    fun getAllNoticeNotis(): LiveData<List<NoticeNoti>>

    fun getIsNotificationActive(): Boolean

    fun setIsNotificationActive(bool: Boolean)
}

@Singleton
class NoticeNotiRepositoryImpl
@Inject
constructor(
    private val noticeNotiDao: NoticeNotiDao,
    @ApplicationContext appContext: Context
) : NoticeNotiRepository {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
        appContext
    )

    override fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable {
        return noticeNotiDao.insertNoticeNoti(noticeNoti)
    }

    override fun deleteNoticeNoti(noticeNoti: NoticeNoti): Completable {
        return noticeNotiDao.deleteNoticeNoti(noticeNoti)
    }

    override fun getAllNoticeNotis(): LiveData<List<NoticeNoti>> {
        return noticeNotiDao.getAllNoticeNotis()
    }

    override fun getIsNotificationActive(): Boolean {
        var result = pref.getString(IS_NOTIFICATION_ACTIVE_KEY, null)
        if (result == null) {
            setIsNotificationActive(true)
            result = true.toString()
        }
        return result.toBoolean()
    }

    override fun setIsNotificationActive(bool: Boolean) {
        pref.edit {
            putString(IS_NOTIFICATION_ACTIVE_KEY, bool.toString())
        }
    }
}
