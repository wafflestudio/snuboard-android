package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.model.NoticeList
import com.wafflestudio.snuboard.domain.usecase.GetNoticesByFollowUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeFeedFragmentViewModel
@Inject
constructor(
        private val getNoticesByFollowUseCase: GetNoticesByFollowUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _notices = MutableLiveData<List<Notice>>()
    val updateNotices = getNoticesByFollowUseCase.updateNotices

    val notices: LiveData<List<Notice>>
        get() = _notices

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    fun getNotices() {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        getNoticesByFollowUseCase
                .getNotices(paginationLimit, paginationCursor)
                .subscribe({
                    when (it) {
                        is NoticeList -> {
                            tmpNoticeList.addAll(it.notices)
                            _notices.value = tmpNoticeList
                            paginationCursor = it.nextCursor
                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }

    fun updateNotices() {
        paginationCursor = null
        getNoticesByFollowUseCase
                .getNotices(paginationLimit, paginationCursor)
                .subscribe({
                    when (it) {
                        is NoticeList -> {
                            _notices.value = it.notices
                            paginationCursor = it.nextCursor
                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }
}