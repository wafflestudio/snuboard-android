package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.model.NoticeList
import com.wafflestudio.snuboard.domain.usecase.*
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeSearchActivityViewModel
@Inject
constructor(
        private val getNoticesByFollowUseCase: GetNoticesByFollowUseCase,
        private val getNoticesOfFollowSearchUseCase: GetNoticeOfFollowSearchUseCase,
        private val getNoticesOfScrapUseCase: GetNoticesOfScrapUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _notices = MutableLiveData<List<Notice>>()
    val keywords = MutableLiveData("")
    val updateNotice = getNoticesOfFollowSearchUseCase.updateNotice

    val notices: LiveData<List<Notice>>
        get() = _notices

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    fun cleanCursor() {
        paginationCursor = null
    }

    fun cleanText() {
        keywords.value = ""
    }

    fun clearNotices() {
        _notices.value = listOf()
    }

    fun getNotices() {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        if (paginationCursor == EOP)
            return
        keywords.value?.apply {
            if (isEmpty())
                return
        }
                ?.let { keyword_string ->
                    getNoticesOfFollowSearchUseCase
                            .getNotices(keyword_string, paginationLimit, paginationCursor)
                            .subscribe({
                                when (it) {
                                    is NoticeList -> {
                                        tmpNoticeList.addAll(it.notices)
                                        _notices.value = tmpNoticeList
                                        paginationCursor = if (it.nextCursor.isEmpty())
                                            EOP
                                        else
                                            it.nextCursor
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

    private fun updateNotice(notice: Notice) {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        _notices.value = tmpNoticeList.map {
            if (it.id == notice.id)
                notice
            else
                it
        }
    }

    fun observeUpdateNotice(event: Event<List<Notice>>) {
        event.getContentIfNotHandled()?.let {
            it.forEach { notice ->
                updateNotice(notice)
            }
        }
    }


    fun toggleSavedNotice(noticeId: Int) {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        tmpNoticeList.find {
            it.id == noticeId
        }?.let {
            if (it.isScrapped)
                deleteNoticeScrapUseCase
                        .deleteNoticeScrapSimple(noticeId)
            else
                postNoticeScrapUseCase
                        .postNoticeScrapSimple(noticeId)
        }
                ?.subscribe({
                    when (it) {
                        is Notice -> {
                            _notices.value = _notices.value!!.map { it1 ->
                                if (it1.id == noticeId)
                                    it
                                else
                                    it1
                            }
                            getNoticesByFollowUseCase.updateNotice(it)
                            getNoticesOfScrapUseCase.updateNotices()
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

    companion object {
        // Used to indicate cursor that it is End of Page
        private const val EOP = "EOP"

    }
}
