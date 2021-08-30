package com.wafflestudio.snuboard.domain.usecase

import com.wafflestudio.snuboard.data.repository.NoticeNotiRepository
import com.wafflestudio.snuboard.data.repository.UserRepository
import com.wafflestudio.snuboard.data.room.NoticeNoti
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotifyUseCase
@Inject
constructor(
        private val noticeNotiRepository: NoticeNotiRepository
) {

    fun deleteNoticeNoti(id: Int): Completable {
        return noticeNotiRepository.deleteNoticeNoti(id)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun addNoticeNoti(
            id: Int,
            title: String,
            departmentId: Int,
            departmentName: String,
            preview: String,
            tags: String,
            postWork: () -> Unit = {}
    ): Completable {
        return doesNoticeExist(id).observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    if (!it) {
                        postWork()
                        return@flatMapCompletable noticeNotiRepository.insertNoticeNoti(NoticeNoti(
                                id,
                                title,
                                departmentId,
                                departmentName,
                                preview,
                                tags,
                                0
                        )).observeOn(AndroidSchedulers.mainThread())
                    } else {
                        throw error("error")
                    }
                }
    }

    fun deleteAllNoticeNotis(): Completable {
        return noticeNotiRepository.deleteAllNoticeNotis()
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getIsNotificationActiveWithDepartment(departmentId: Int): Boolean {
        return noticeNotiRepository.getIsNotificationActiveWithDepartment(departmentId)
    }

    fun setIsNotificationActiveWithDepartment(departmentId: Int, bool: Boolean) {
        noticeNotiRepository.setIsNotificationActiveWithDepartment(departmentId, bool)
    }

    private fun doesNoticeExist(id: Int): Single<Boolean> {
        return noticeNotiRepository.doesNoticeExist(id)
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class FCMTopicUseCase
@Inject
constructor(
    private val userRepository: UserRepository
) {

    fun subscribeAll(fcmToken: String): Single<Any> {
        return userRepository
                .subscribeToMyFCMTopics(fcmToken)
                .observeOn(AndroidSchedulers.mainThread())
    }
}
