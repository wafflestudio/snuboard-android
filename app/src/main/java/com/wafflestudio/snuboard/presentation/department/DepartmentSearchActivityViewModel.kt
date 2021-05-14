package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.*
import com.wafflestudio.snuboard.domain.usecase.DeleteNoticeScrapUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfDepartmentIdSearchUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfDepartmentUseCase
import com.wafflestudio.snuboard.domain.usecase.PostNoticeScrapUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentSearchActivityViewModel
@Inject
constructor(
        private val getNoticesOfDepartmentUseCase: GetNoticesOfDepartmentUseCase,
        private val getNoticesOfDepartmentIdSearchUseCase: GetNoticesOfDepartmentIdSearchUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _notices = MutableLiveData<List<Notice>>()
    val keywords = MutableLiveData("")
    val updateNotice = getNoticesOfDepartmentIdSearchUseCase.updateNotice

    val notices: LiveData<List<Notice>>
        get() = _notices

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    private val departmentInfo = MutableLiveData<TagDepartment>()


    fun initiateDepartment(tagDepartmentInfo: TagDepartmentFull) {
        val newTags = tagDepartmentInfo.tags.map {
            Tag(it.content, DepartmentColor.TAG_COLOR)
        }
        departmentInfo.value = TagDepartment(
                tagDepartmentInfo.id,
                tagDepartmentInfo.name,
                newTags,
                tagDepartmentInfo.departmentColor
        )
    }

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
                    getNoticesOfDepartmentIdSearchUseCase
                            .getNotices(departmentInfo.value!!.id,
                                    keyword_string,
                                    paginationLimit,
                                    paginationCursor,
                                    departmentInfo.value!!.tags.map { it.content })
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
                            getNoticesOfDepartmentUseCase.updateNotice(it)
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
