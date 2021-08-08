package com.wafflestudio.snuboard.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable

@Dao
interface NoticeNotiDao {

    @Insert
    fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable

    @Delete
    fun deleteNoticeNoti(noticeNoti: NoticeNoti): Completable

    @Query("SELECT * FROM notice_noti_table ORDER BY id ASC")
    fun getAllNoticeNotis(): LiveData<List<NoticeNoti>>
}
