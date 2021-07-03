package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.NoticeDetail
import com.wafflestudio.snuboard.domain.usecase.*
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoticeDetailActivityViewModel
@Inject
constructor(
        private val getNoticeByIdUseCase: GetNoticeByIdUseCase,
        private val getNoticesByFollowUseCase: GetNoticesByFollowUseCase,
        private val getNoticesOfFollowSearchUseCase: GetNoticeOfFollowSearchUseCase,
        private val getNoticesOfScrapUseCase: GetNoticesOfScrapUseCase,
        private val getNoticesOfDepartmentUseCase: GetNoticesOfDepartmentUseCase,
        private val getNoticesOfDepartmentIdSearchUseCase: GetNoticesOfDepartmentIdSearchUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _notice = MutableLiveData<NoticeDetail>()
    val notice: LiveData<NoticeDetail>
        get() = _notice

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getNotice(noticeId: Int) {
        _isLoading.value = true
        getNoticeByIdUseCase
                .getNotice(noticeId)
                .subscribe({
                    _isLoading.value = false
                    when (it) {
                        is NoticeDetail -> {
                            _notice.value = it
                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                        }
                    }
                }, {
                    _isLoading.value = false
                    Timber.e(it)
                })
    }

    fun toggleSavedNotice() {
        notice.value?.let {
            if (it.isScrapped)
                deleteNoticeScrapUseCase
                        .deleteNoticeScrap(it.id)
            else
                postNoticeScrapUseCase
                        .postNoticeScrap(it.id)
        }
                ?.subscribe({
                    when (it) {
                        is NoticeDetail -> {
                            _notice.value = it
                            getNoticesByFollowUseCase.updateNoticeWithNoticeDetail(it)
                            getNoticesOfFollowSearchUseCase.updateNoticeWithNoticeDetail(it)
                            getNoticesOfDepartmentUseCase.updateNoticeWithNoticeDetail(it)
                            getNoticesOfDepartmentIdSearchUseCase.updateNoticeWithNoticeDetail(it)
                            if (!it.isScrapped)
                                getNoticesOfScrapUseCase.updateNoticeWithNoticeDetail(it)
                            else
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
}
