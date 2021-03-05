package com.wafflestudio.snuboard.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wafflestudio.snuboard.data.repository.NoticeRepository
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.utils.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNoticesByFollowUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository
) {

    private val _updateNotices = MutableLiveData<Event<Unit>>()
    val updateNotices: LiveData<Event<Unit>>
        get() = _updateNotices

    fun getNotices(limit: Int, cursor: String?): Single<Any> {
        return noticeRepository
                .getNoticesOfFollow(limit, cursor)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNotices() {
        _updateNotices.value = Event(Unit)
    }
}

@Singleton
class GetNoticesOfScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository
) {

    private val _updateNotices = MutableLiveData<Event<Unit>>()
    private val _updateNotice = MutableLiveData<Event<Notice>>()

    val updateNotices: LiveData<Event<Unit>>
        get() = _updateNotices
    val updateNotice: LiveData<Event<Notice>>
        get() = _updateNotice

    fun getNotices(limit: Int, cursor: String?): Single<Any> {
        return noticeRepository
                .getNoticesOfScrap(limit, cursor)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNotices() {
        _updateNotices.value = Event(Unit)
    }


    fun updateNotice(notice: Notice) {
        _updateNotice.value = Event(notice)
    }
}

@Singleton
class DeleteNoticeScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository
) {
    fun deleteNoticeScrap(noticeId: Int): Single<Any> {
        return noticeRepository
                .deleteNoticeScrap(noticeId)
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class PostNoticeScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository
) {
    fun postNoticeScrap(noticeId: Int): Single<Any> {
        return noticeRepository
                .postNoticeScrap(noticeId)
                .observeOn(AndroidSchedulers.mainThread())
    }
}