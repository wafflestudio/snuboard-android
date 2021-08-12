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

    private val _isNewLoading = MutableLiveData<Boolean>(false)
    private val _isAddLoading = MutableLiveData<Boolean>(false)
    private val _isPageEnd = MutableLiveData<Boolean>(false)

    val savedNotices: LiveData<List<Notice>>
        get() = _savedNotices
    val isNewLoading: LiveData<Boolean>
        get() = _isNewLoading
    val isAddLoading: LiveData<Boolean>
        get() = _isAddLoading
    val isPageEnd: LiveData<Boolean>
        get() = _isPageEnd

    private val savedNoticesPaginationLimit = 10
    private var savedNoticesPaginationCursor: String? = null

    fun getSavedNotices() {
        val tmpNoticeList = _savedNotices.value?.toMutableList() ?: mutableListOf()
        if (savedNoticesPaginationCursor == EOP) {
            _isPageEnd.value = true
            return
        }
        if (tmpNoticeList.isEmpty())
            _isNewLoading.value = true
        else
            _isAddLoading.value = true
        getNoticesOfScrapUseCase
                .getNotices(savedNoticesPaginationLimit, savedNoticesPaginationCursor)
                .subscribe({
                    _isNewLoading.value = false
                    _isAddLoading.value = false
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
                    _isNewLoading.value = false
                    _isAddLoading.value = false
                    SingleEvent.triggerToast.value = Event("서버 관련 에러가 발생했습니다")
                    Timber.e(it)
                })
    }

    fun updateSavedNotices(callback: (() -> Unit)? = null) {
        savedNoticesPaginationCursor = null
        _isPageEnd.value = false
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
                    callback?.let { it() }
                }, {
                    Timber.e(it)
                    callback?.let { it() }
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
