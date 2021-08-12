package com.wafflestudio.snuboard.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoticeNotiDao {

    @Insert
    fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable

    @Query("SELECT EXISTS(SELECT * FROM notice_noti_table WHERE id= :id)")
    fun doesNoticeExist(id: Int): Single<Boolean>

    @Query("UPDATE notice_noti_table SET isDeleted = 1 WHERE id = :id")
    fun deleteNoticeNoti(id: Int): Completable

    @Query("SELECT * FROM notice_noti_table WHERE isDeleted = 0 ORDER BY id DESC ")
    fun getAllNoticeNotis(): LiveData<List<NoticeNoti>>
}
