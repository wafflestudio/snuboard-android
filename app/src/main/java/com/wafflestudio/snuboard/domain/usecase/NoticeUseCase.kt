package com.wafflestudio.snuboard.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wafflestudio.snuboard.data.repository.NoticeRepository
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.model.NoticeDetail
import com.wafflestudio.snuboard.domain.translater.NoticeMapper
import com.wafflestudio.snuboard.utils.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNoticesOfDepartmentIdSearchUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {

    private val _updateNotice = MutableLiveData<Event<List<Notice>>>(Event(listOf()))

    val updateNotice: LiveData<Event<List<Notice>>>
        get() = _updateNotice

    fun getNotices(departmentId: Int, keywords: String?, limit: Int, cursor: String?, tags: List<String>): Single<Any> {
        return noticeRepository
                .getNoticesOfDepartmentIdSearch(
                        departmentId,
                        keywords,
                        limit,
                        cursor,
                        null,
                        tags.joinToString(","))
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNoticeWithNoticeDetail(noticeDetail: NoticeDetail) {
        val notice = noticeMapper.mapToNoticeFromNoticeDetail(noticeDetail)
        _updateNotice.value?.let {
            if (it.hasBeenHandled) {
                _updateNotice.value = Event(listOf(notice))
            } else {
                it.peekContent()?.let { it1 ->
                    val tmpUpdateNotice = it1.toMutableList()
                    tmpUpdateNotice.add(notice)
                    _updateNotice.value = Event(tmpUpdateNotice)
                }
            }
        }
    }

}

@Singleton
class GetNoticesByFollowUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {

    private val _updateNotices = MutableLiveData<Event<Unit>>()
    private val _updateNotice = MutableLiveData<Event<List<Notice>>>(Event(listOf()))

    val updateNotices: LiveData<Event<Unit>>
        get() = _updateNotices
    val updateNotice: LiveData<Event<List<Notice>>>
        get() = _updateNotice

    fun getNotices(limit: Int, cursor: String?): Single<Any> {
        return noticeRepository
                .getNoticesOfFollow(limit, cursor)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNotices() {
        _updateNotices.value = Event(Unit)
    }

    fun updateNotice(notice: Notice) {
        _updateNotice.value?.let {
            if (it.hasBeenHandled) {
                _updateNotice.value = Event(listOf(notice))
            } else {
                it.peekContent()?.let { it1 ->
                    val tmpUpdateNotice = it1.toMutableList()
                    tmpUpdateNotice.add(notice)
                    _updateNotice.value = Event(tmpUpdateNotice)
                }
            }
        }
    }

    fun updateNoticeWithNoticeDetail(noticeDetail: NoticeDetail) {
        val notice = noticeMapper.mapToNoticeFromNoticeDetail(noticeDetail)
        _updateNotice.value?.let {
            if (it.hasBeenHandled) {
                _updateNotice.value = Event(listOf(notice))
            } else {
                it.peekContent()?.let { it1 ->
                    val tmpUpdateNotice = it1.toMutableList()
                    tmpUpdateNotice.add(notice)
                    _updateNotice.value = Event(tmpUpdateNotice)
                }
            }
        }
    }
}

@Singleton
class GetNoticeOfFollowSearchUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {

    private val _updateNotice = MutableLiveData<Event<List<Notice>>>(Event(listOf()))

    val updateNotice: LiveData<Event<List<Notice>>>
        get() = _updateNotice

    fun getNotices(keywords: String, limit: Int, cursor: String?): Single<Any> {
        return noticeRepository
                .getNoticeOfFollowSearch(keywords, limit, cursor)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNoticeWithNoticeDetail(noticeDetail: NoticeDetail) {
        val notice = noticeMapper.mapToNoticeFromNoticeDetail(noticeDetail)
        _updateNotice.value?.let {
            if (it.hasBeenHandled) {
                _updateNotice.value = Event(listOf(notice))
            } else {
                it.peekContent()?.let { it1 ->
                    val tmpUpdateNotice = it1.toMutableList()
                    tmpUpdateNotice.add(notice)
                    _updateNotice.value = Event(tmpUpdateNotice)
                }
            }
        }
    }

}

@Singleton
class GetNoticesOfScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {

    private val _updateNotices = MutableLiveData<Event<Unit>>()
    private val _updateNotice = MutableLiveData<Event<List<Notice>>>(Event(listOf()))

    val updateNotices: LiveData<Event<Unit>>
        get() = _updateNotices
    val updateNotice: LiveData<Event<List<Notice>>>
        get() = _updateNotice

    fun getNotices(limit: Int, cursor: String?): Single<Any> {
        return noticeRepository
                .getNoticesOfScrap(limit, cursor)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNotices() {
        _updateNotices.value = Event(Unit)
    }

    fun updateNoticeWithNoticeDetail(noticeDetail: NoticeDetail) {
        val notice = noticeMapper.mapToNoticeFromNoticeDetail(noticeDetail)
        _updateNotice.value?.let {
            if (it.hasBeenHandled) {
                _updateNotice.value = Event(listOf(notice))
            } else {
                it.peekContent()?.let { it1 ->
                    val tmpUpdateNotice = it1.toMutableList()
                    tmpUpdateNotice.add(notice)
                    _updateNotice.value = Event(tmpUpdateNotice)
                }
            }
        }
    }

}

@Singleton
class GetNoticeByIdUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository
) {

    fun getNotice(noticeId: Int): Single<Any> {
        return noticeRepository
                .getNoticeById(noticeId)
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class DeleteNoticeScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {
    fun deleteNoticeScrap(noticeId: Int): Single<Any> {
        return noticeRepository
                .deleteNoticeScrap(noticeId)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteNoticeScrapSimple(noticeId: Int): Single<Any> {
        return noticeRepository
                .deleteNoticeScrap(noticeId)
                .map {
                    when (it) {
                        is NoticeDetail ->
                            return@map noticeMapper.mapToNoticeFromNoticeDetail(it)
                        else ->
                            return@map it
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}

@Singleton
class PostNoticeScrapUseCase
@Inject
constructor(
        private val noticeRepository: NoticeRepository,
        private val noticeMapper: NoticeMapper
) {
    fun postNoticeScrap(noticeId: Int): Single<Any> {
        return noticeRepository
                .postNoticeScrap(noticeId)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun postNoticeScrapSimple(noticeId: Int): Single<Any> {
        return noticeRepository
                .postNoticeScrap(noticeId)
                .map {
                    when (it) {
                        is NoticeDetail ->
                            return@map noticeMapper.mapToNoticeFromNoticeDetail(it)
                        else ->
                            return@map it
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
    }
}
