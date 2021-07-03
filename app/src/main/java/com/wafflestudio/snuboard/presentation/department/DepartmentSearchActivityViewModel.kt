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
    private val _isNewLoading = MutableLiveData<Boolean>(false)
    private val _isAddLoading = MutableLiveData<Boolean>(false)
    private val _isPageEnd = MutableLiveData<Boolean>(false)
    val keywords = MutableLiveData("")
    val updateNotice = getNoticesOfDepartmentIdSearchUseCase.updateNotice

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

    private val _tagDepartmentInfo = MutableLiveData<TagDepartmentFull>()
    val tagDepartmentInfo: LiveData<TagDepartmentFull>
        get() = _tagDepartmentInfo


    // Tag members
    private var homeTagString = listOf<String>()
    private var homeTagStringFixed = listOf<String>()

    private val _isHomeTagsVisible = MutableLiveData(false)
    val isHomeTagsVisible: LiveData<Boolean>
        get() = _isHomeTagsVisible

    private val _isFilterOn = MutableLiveData(false)
    val isFilterOn: LiveData<Boolean>
        get() = _isFilterOn

    private val _notifyFilterNoticeList = MutableLiveData<Event<Unit>>()
    val notifyFilterNoticeList: LiveData<Event<Unit>>
        get() = _notifyFilterNoticeList


    // Tag Functions


    private fun notifyList() {
        _notifyFilterNoticeList.value = Event(Unit)
    }

    private fun getHomeTagString() {
        val tmpHomeTagString = mutableListOf<String>()
        tmpHomeTagString.addAll(homeTagStringFixed)
        homeTagString = tmpHomeTagString
        _isFilterOn.value = homeTagStringFixed.isNotEmpty()
        notifyList()
    }

    fun toggleHomeTag(tagContent: String) {
        homeTagString = if (tagContent in homeTagString) {
            homeTagString.filter { it != tagContent }
        } else {
            val mutableHomeTagString = homeTagString.toMutableList()
            mutableHomeTagString.add(tagContent)
            mutableHomeTagString
        }

        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!

        val homeTags = tmpTagDepartmentInfo.homeTags.map {
            if (it.content in homeTagString) {
                Tag(it.content, DepartmentColor.TAG_SELECTED_COLOR)
            } else {
                Tag(it.content, DepartmentColor.TAG_COLOR)
            }
        }

        val tagDepartmentFull = TagDepartmentFull(
            tmpTagDepartmentInfo.id,
            tmpTagDepartmentInfo.name,
            tmpTagDepartmentInfo.tags,
            homeTags,
            tmpTagDepartmentInfo.departmentColor
        )

        _tagDepartmentInfo.value = tagDepartmentFull
        notifyList()
    }

    fun toggleHomeTagCard() {
        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!
        getHomeTagString()
        if (isHomeTagsVisible.value!!) {
            val homeTags = tmpTagDepartmentInfo.homeTags.map {
                if (it.content in homeTagString) {
                    Tag(it.content, DepartmentColor.TAG_SELECTED_COLOR)
                } else {
                    Tag(it.content, DepartmentColor.TAG_COLOR)
                }
            }

            val tagDepartmentFull = TagDepartmentFull(
                tmpTagDepartmentInfo.id,
                tmpTagDepartmentInfo.name,
                tmpTagDepartmentInfo.tags,
                homeTags,
                tmpTagDepartmentInfo.departmentColor
            )
            _tagDepartmentInfo.value = tagDepartmentFull
        }
        _isHomeTagsVisible.value = !isHomeTagsVisible.value!!
        notifyList()
    }

    fun applyHomeTags() {
        val tmpHomeTagString = homeTagString.toMutableList()
        homeTagStringFixed = tmpHomeTagString
        _isFilterOn.value = homeTagStringFixed.isNotEmpty()
        updateNotices {
            SingleEvent.triggerToast.value = Event("필터를 적용하였습니다.")
        }
        if (keywords.value!!.isBlank()) {
            SingleEvent.triggerToast.value = Event("필터를 적용하였습니다.")
            notifyList()
        }
    }

    fun eraseHomeTags() {
        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!
        homeTagString = listOf()
        homeTagStringFixed = listOf()
        val homeTags = tmpTagDepartmentInfo.homeTags.map {
            if (it.content in homeTagString) {
                Tag(it.content, DepartmentColor.TAG_SELECTED_COLOR)
            } else {
                Tag(it.content, DepartmentColor.TAG_COLOR)
            }
        }
        val tagDepartmentFull = TagDepartmentFull(
            tmpTagDepartmentInfo.id,
            tmpTagDepartmentInfo.name,
            tmpTagDepartmentInfo.tags,
            homeTags,
            tmpTagDepartmentInfo.departmentColor
        )
        _tagDepartmentInfo.value = tagDepartmentFull
        _isFilterOn.value = homeTagStringFixed.isNotEmpty()
        updateNotices {
            SingleEvent.triggerToast.value = Event("필터를 제거하였습니다.")
        }
        if (keywords.value!!.isBlank()) {
            SingleEvent.triggerToast.value = Event("필터를 제거하였습니다.")
            notifyList()
        }
    }

    // Search Functions
    fun initiateDepartment(tagDepartmentInfo: TagDepartmentFull) {
        val newTags = tagDepartmentInfo.tags.map {
            Tag(it.content, DepartmentColor.TAG_COLOR)
        }
        _tagDepartmentInfo.value = TagDepartmentFull(
            tagDepartmentInfo.id,
            tagDepartmentInfo.name,
            tagDepartmentInfo.tags,
            newTags,
            tagDepartmentInfo.departmentColor
        )
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

    fun getNotices() {
        val tmpNoticeList = _notices.value?.toMutableList() ?: mutableListOf()
        if (paginationCursor == EOP) {
            _isPageEnd.value = true
            return
        }
        if (tmpNoticeList.isEmpty())
            _isNewLoading.value = true
        else
            _isAddLoading.value = true
        keywords.value?.apply {
            if (isEmpty())
                return
        }
                ?.let { keyword_string ->
                    getNoticesOfDepartmentIdSearchUseCase
                            .getNotices(
                                    tagDepartmentInfo.value!!.id,
                                    keyword_string,
                                    paginationLimit,
                                    paginationCursor,
                                    homeTagStringFixed
                            )
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
    }

    private fun updateNotices(postWork: () -> Unit = {}) {
        paginationCursor = null
        _isPageEnd.value = false
        keywords.value?.apply {
            if (isEmpty())
                return
        }
            ?.let { keyword_string ->
                getNoticesOfDepartmentIdSearchUseCase
                    .getNotices(
                        tagDepartmentInfo.value!!.id,
                        keyword_string,
                        paginationLimit,
                        paginationCursor,
                        homeTagStringFixed
                    )
                    .subscribe({
                        when (it) {
                            is NoticeList -> {
                                _notices.value = it.notices
                                paginationCursor = if (it.nextCursor.isEmpty())
                                    EOP
                                else
                                    it.nextCursor
                                postWork()
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
