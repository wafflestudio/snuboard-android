package com.wafflestudio.snuboard.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.model.NoticeList
import com.wafflestudio.snuboard.domain.usecase.DeleteNoticeScrapUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesByFollowUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfScrapUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SavedFragmentViewModel
@Inject
constructor(
        private val getNoticesByFollowUseCase: GetNoticesByFollowUseCase,
        private val getNoticesOfScrapUseCase: GetNoticesOfScrapUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _savedNotices = MutableLiveData<List<Notice>>()
    val updateSavedNotices = getNoticesOfScrapUseCase.updateNotices
    val updateSavedNotice = getNoticesOfScrapUseCase.updateNotice

    val savedNotices: LiveData<List<Notice>>
        get() = _savedNotices

    private val savedNoticesPaginationLimit = 10
    private var savedNoticesPaginationCursor: String? = null

    fun getSavedNotices() {
        val tmpNoticeList = _savedNotices.value?.toMutableList() ?: mutableListOf()
        if (savedNoticesPaginationCursor == EOP)
            return
        getNoticesOfScrapUseCase
                .getNotices(savedNoticesPaginationLimit, savedNoticesPaginationCursor)
                .subscribe({
                    when (it) {
                        is NoticeList -> {
                            tmpNoticeList.addAll(it.notices)
                            _savedNotices.value = tmpNoticeList
                            savedNoticesPaginationCursor = if (it.nextCursor.isEmpty())
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

    fun updateSavedNotices() {
        savedNoticesPaginationCursor = null
        getNoticesOfScrapUseCase
                .getNotices(savedNoticesPaginationLimit, savedNoticesPaginationCursor)
                .subscribe({
                    when (it) {
                        is NoticeList -> {
                            _savedNotices.value = it.notices
                            savedNoticesPaginationCursor = if (it.nextCursor.isEmpty())
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

    private fun updateSavedNotice(notice: Notice) {
        val tmpNoticeList = _savedNotices.value?.toMutableList() ?: mutableListOf()
        if (!notice.isScrapped)
            _savedNotices.value = tmpNoticeList.filter { it1 ->
                notice.id != it1.id
            }
    }

    fun observeUpdateNotice(event: Event<List<Notice>>) {
        event.getContentIfNotHandled()?.let {
            it.forEach { notice ->
                updateSavedNotice(notice)
            }
        }
    }

    fun toggleSavedNotice(noticeId: Int) {
        val tmpNoticeList = _savedNotices.value?.toMutableList() ?: mutableListOf()
        tmpNoticeList.find {
            it.id == noticeId
        }?.let {
            if (it.isScrapped)
                deleteNoticeScrapUseCase
                        .deleteNoticeScrapSimple(noticeId)
            else
                throw Error("App logic error. Notice should be scrapped in this page.")
        }
                ?.subscribe({
                    when (it) {
                        is Notice -> {
                            _savedNotices.value = _savedNotices.value!!.filter { it1 ->
                                it.id != it1.id
                            }
                            getNoticesByFollowUseCase.updateNotice(it)
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
