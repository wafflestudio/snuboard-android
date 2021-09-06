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
    private val _isNewLoading = MutableLiveData<Boolean>(false)
    private val _isAddLoading = MutableLiveData<Boolean>(false)
    private val _isPageEnd = MutableLiveData<Boolean>(false)
    val keywords = MutableLiveData("")

    private var fixedKeywords = ""

    val updateNotice = getNoticesOfFollowSearchUseCase.updateNotice

    val notices: LiveData<List<Notice>>
        get() = _notices
    val isNewLoading: LiveData<Boolean>
        get() = _isNewLoading
    val isAddLoading: LiveData<Boolean>
        get() = _isAddLoading
    val isPageEnd: LiveData<Boolean>
        get() = _isPageEnd

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    fun fixKeywords() {
        keywords.value?.let {
            fixedKeywords = it
        }
    }

    fun cleanCursor() {
        paginationCursor = null
        _isPageEnd.value = false
    }

    fun cleanText() {
        keywords.value = ""
    }

    fun clearNotices() {
        _notices.value = listOf()
    }

    fun getNotices(callback: (() -> Unit)? = null) {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        if (paginationCursor == EOP) {
            _isPageEnd.value = true
            return
        }
        if (tmpNoticeList.isEmpty())
            _isNewLoading.value = true
        else
            _isAddLoading.value = true
        fixedKeywords.apply {
            if (isEmpty()) {
                _isNewLoading.value = false
                _isAddLoading.value = false
                SingleEvent.triggerToast.value = Event("빈 검색어로 검색할 수 없습니다")
                return
            }
        }
                .let { keyword_string ->
                    getNoticesOfFollowSearchUseCase
                            .getNotices(keyword_string, paginationLimit, paginationCursor)
                            .subscribe({
                                _isNewLoading.value = false
                                _isAddLoading.value = false
                                when (it) {
                                    is NoticeList -> {
                                        tmpNoticeList.addAll(it.notices)
                                        _notices.value = tmpNoticeList
                                        paginationCursor = if (it.nextCursor.isEmpty())
                                            EOP
                                        else
                                            it.nextCursor
                                        if (it.notices.isEmpty())
                                            SingleEvent.triggerToast.value = Event("검색어가 들어있는 게시물이 없습니다")
                                    }
                                    is ErrorResponse -> {
                                        SingleEvent.triggerToast.value = Event(it.message)
                                        Timber.e(it.message)
                                    }
                                }
                                callback?.let { it() }
                            }, {
                                _isNewLoading.value = false
                                _isAddLoading.value = false
                                SingleEvent.triggerToast.value = Event("서버 관련 에러가 발생했습니다")
                                Timber.e(it)
                                callback?.let { it() }
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
