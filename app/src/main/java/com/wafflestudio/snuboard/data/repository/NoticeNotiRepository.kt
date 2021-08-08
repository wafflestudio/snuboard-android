package com.wafflestudio.snuboard.data.repository

import androidx.lifecycle.LiveData
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.data.room.NoticeNotiDao
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject
import javax.inject.Singleton

interface NoticeNotiRepository {

    fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable

    fun deleteNoticeNoti(noticeNoti: NoticeNoti): Completable

    fun getAllNoticeNotis(): LiveData<List<NoticeNoti>>
}

@Singleton
class NoticeNotiRepositoryImpl
@Inject
constructor(
        private val noticeNotiDao: NoticeNotiDao
) : NoticeNotiRepository {
    override fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable {
        return noticeNotiDao.insertNoticeNoti(noticeNoti)
    }

    override fun deleteNoticeNoti(noticeNoti: NoticeNoti): Completable {
        return noticeNotiDao.deleteNoticeNoti(noticeNoti)
    }

    override fun getAllNoticeNotis(): LiveData<List<NoticeNoti>> {
        return noticeNotiDao.getAllNoticeNotis()
    }

}
