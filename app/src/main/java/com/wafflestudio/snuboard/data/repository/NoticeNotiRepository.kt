package com.wafflestudio.snuboard.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.data.room.NoticeNotiDao
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.di.SharedPreferenceConst.getDepartmentNotiKey
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface NoticeNotiRepository {

    fun insertNoticeNoti(noticeNoti: NoticeNoti): Completable

    fun doesNoticeExist(id: Int): Single<Boolean>

    fun deleteNoticeNoti(id: Int): Completable

    fun getAllNoticeNotis(): LiveData<List<NoticeNoti>>

    fun deleteAllNoticeNotis(): Completable

    fun getIsNotificationActiveWithDepartment(departmentId: Int): Boolean

    fun setIsNotificationActiveWithDepartment(departmentId: Int, bool: Boolean)
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
                .subscribeOn(Schedulers.io())
    }

    override fun doesNoticeExist(id: Int): Single<Boolean> {
        return noticeNotiDao.doesNoticeExist(id)
                .subscribeOn(Schedulers.io())
    }

    override fun deleteNoticeNoti(id: Int): Completable {
        return noticeNotiDao.deleteNoticeNoti(id)
                .subscribeOn(Schedulers.io())
    }

    override fun getAllNoticeNotis(): LiveData<List<NoticeNoti>> {
        return noticeNotiDao.getAllNoticeNotis()
    }

    override fun deleteAllNoticeNotis(): Completable {
        return noticeNotiDao.deleteAllNoticeNotis()
                .subscribeOn(Schedulers.io())
    }

    override fun getIsNotificationActiveWithDepartment(departmentId: Int): Boolean {
        var result = pref.getString(getDepartmentNotiKey(departmentId), null)
        if (result == null) {
            setIsNotificationActiveWithDepartment(departmentId, true)
            result = true.toString()
        }
        if (pref.getString(SharedPreferenceConst.ACCESS_TOKEN_KEY, "none") == "none")
            result = false.toString()
        return result.toBoolean()
    }

    override fun setIsNotificationActiveWithDepartment(departmentId: Int, bool: Boolean) {
        pref.edit {
            putString(getDepartmentNotiKey(departmentId), bool.toString())
        }
    }
}
