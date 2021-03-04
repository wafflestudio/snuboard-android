package com.wafflestudio.snuboard.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wafflestudio.snuboard.data.repository.NoticeRepository
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
}