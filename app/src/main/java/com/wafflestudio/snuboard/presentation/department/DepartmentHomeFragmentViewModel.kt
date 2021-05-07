package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.model.NoticeList
import com.wafflestudio.snuboard.domain.usecase.DeleteNoticeScrapUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfDepartmentUseCase
import com.wafflestudio.snuboard.domain.usecase.PostNoticeScrapUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentHomeFragmentViewModel
@Inject
constructor(
        private val getNoticesOfDepartmentUseCase: GetNoticesOfDepartmentUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _notices = MutableLiveData<List<Notice>>()
    val updateNotices = getNoticesOfDepartmentUseCase.updateNotice

    val notices: LiveData<List<Notice>>
        get() = _notices

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    fun getNotices(departmentId: Int, homeTags: List<String>) {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        if (paginationCursor == EOP)
            return
        getNoticesOfDepartmentUseCase
            .getNotices(departmentId, paginationLimit, paginationCursor, homeTags)
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

    companion object {
        // Used to indicate cursor that it is End of Page
        private const val EOP = "EOP"

    }
}
