package com.wafflestudio.snuboard.presentation.department

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.*
import com.wafflestudio.snuboard.domain.usecase.*
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentActivityViewModel
@Inject
constructor(
        private val getTagDepartmentInfoUseCase: GetTagDepartmentInfoUseCase,
        private val postFollowingTagUseCase: PostFollowingTagUseCase,
        private val deleteFollowingTagUseCase: DeleteFollowingTagUseCase,
        private val getNoticesOfDepartmentUseCase: GetNoticesOfDepartmentUseCase,
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        @ApplicationContext appContext: Context,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    // Loading Status
    private val _isNewLoading = MutableLiveData<Boolean>(false)
    private val _isAddLoading = MutableLiveData<Boolean>(false)
    private val _isPageEnd = MutableLiveData<Boolean>(false)

    val isNewLoading: LiveData<Boolean>
        get() = _isNewLoading
    val isAddLoading: LiveData<Boolean>
        get() = _isAddLoading
    val isPageEnd: LiveData<Boolean>
        get() = _isPageEnd

    // Department members
    private val _tagDepartmentInfo = MutableLiveData<TagDepartmentFull>()
    val tagDepartmentInfo: LiveData<TagDepartmentFull>
        get() = _tagDepartmentInfo

    // Tag members
    private var homeTagString = listOf<String>()

    private val _isHomeTagsVisible = MutableLiveData(false)
    val isHomeTagsVisible: LiveData<Boolean>
        get() = _isHomeTagsVisible

    private val _isFilterOn = MutableLiveData(false)
    val isFilterOn: LiveData<Boolean>
        get() = _isFilterOn

    private val _notifyFilterNoticeList = MutableLiveData<Event<Unit>>()
    val notifyFilterNoticeList: LiveData<Event<Unit>>
        get() = _notifyFilterNoticeList

    // Notice Members
    private val _notices = MutableLiveData<List<Notice>>()
    val updateNotice = getNoticesOfDepartmentUseCase.updateNotice

    val notices: LiveData<List<Notice>>
        get() = _notices

    private val paginationLimit = 10
    private var paginationCursor: String? = null

    // Department Functions
    fun getTagDepartmentInfo(departmentId: Int) {
        getHomeTagString(departmentId)
        getTagDepartmentInfoUseCase
                .getTagDepartmentInfo(departmentId)
                .subscribe({
                    when (it) {
                        is TagDepartment -> {
                            var homeTags = listOf<Tag>()
                            tagDepartmentInfo.value?.also { org ->
                                homeTags = org.homeTags
                            } ?: run {
                                homeTags = it.tags.map { tag ->
                                    if (tag.content in homeTagString) {
                                        Tag(tag.content, DepartmentColor.TAG_SELECTED_COLOR)
                                    } else {
                                        Tag(tag.content, DepartmentColor.TAG_COLOR)
                                    }
                                }
                            }
                            val tagDepartmentFull = TagDepartmentFull(
                                    it.id,
                                    it.name,
                                    it.link,
                                    it.tags,
                                    homeTags,
                                    it.departmentColor
                            )

                            _tagDepartmentInfo.value = tagDepartmentFull
                            getNotices()
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

    fun changeDepartmentColor(departmentColor: DepartmentColor) {
        val tmpTagDepartment = tagDepartmentInfo.value!!
        val preferenceKey = SharedPreferenceConst.getDepartmentColorKey(tmpTagDepartment.id)
        pref.edit {
            putInt(preferenceKey, departmentColor.colorId)
        }
        _tagDepartmentInfo.value = TagDepartmentFull(
                tmpTagDepartment.id,
                tmpTagDepartment.name,
                tmpTagDepartment.link,
                tmpTagDepartment.tags,
                tmpTagDepartment.homeTags,
                departmentColor
        )
    }

    // Tag Functions

    private fun notifyList() {
        _notifyFilterNoticeList.value = Event(Unit)
    }

    private fun getHomeTagString(departmentId: Int) {
        val preferenceKey = SharedPreferenceConst.getDepartmentHomeKey(departmentId)
        val departmentHomeTagString = pref.getString(preferenceKey, "EMPTY")
        var departmentHomeTags = listOf<String>()
        if (departmentHomeTagString == "EMPTY" || departmentHomeTagString!!.isBlank()) {
            pref.edit {
                putString(preferenceKey, "")
            }
        } else {
            departmentHomeTags = departmentHomeTagString.split(",")
        }
        homeTagString = departmentHomeTags
        _isFilterOn.value = homeTagString.isNotEmpty()
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
                tmpTagDepartmentInfo.link,
                tmpTagDepartmentInfo.tags,
                homeTags,
                tmpTagDepartmentInfo.departmentColor
        )

        _tagDepartmentInfo.value = tagDepartmentFull
        notifyList()
    }

    fun toggleHomeTagCard() {
        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!

        if (isHomeTagsVisible.value!!) {
            getHomeTagString(tmpTagDepartmentInfo.id)
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
                    tmpTagDepartmentInfo.link,
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
        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!
        val preferenceKey = SharedPreferenceConst.getDepartmentHomeKey(tmpTagDepartmentInfo.id)
        pref.edit {
            putString(preferenceKey, homeTagString.joinToString(separator = ","))
        }
        _isFilterOn.value = homeTagString.isNotEmpty()
        updateNotices {
            SingleEvent.triggerToast.value = Event("필터를 적용하였습니다.")
        }
    }

    fun eraseHomeTags() {
        val tmpTagDepartmentInfo = tagDepartmentInfo.value!!
        val preferenceKey = SharedPreferenceConst.getDepartmentHomeKey(tmpTagDepartmentInfo.id)
        pref.edit {
            putString(preferenceKey, "")
        }
        homeTagString = listOf()
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
                tmpTagDepartmentInfo.link,
                tmpTagDepartmentInfo.tags,
                homeTags,
                tmpTagDepartmentInfo.departmentColor
        )
        _tagDepartmentInfo.value = tagDepartmentFull
        _isFilterOn.value = homeTagString.isNotEmpty()
        updateNotices {
            SingleEvent.triggerToast.value = Event("필터를 제거하였습니다.")
        }
    }


    fun toggleFollowingTag(tagContent: String) {
        tagDepartmentInfo.value?.let {
            val designatedColor = it.tags.find { tag -> tag.content == tagContent }?.color
            when (designatedColor) {
                DepartmentColor.TAG_COLOR ->
                    postFollowingTagUseCase
                            .postFollowingTag(it.id, tagContent)
                DepartmentColor.TAG_SELECTED_COLOR ->
                    deleteFollowingTagUseCase
                            .deleteFollowingTag(it.id, tagContent)
                else ->
                    null
            }
                    ?.subscribe({ it1 ->
                        when (it1) {
                            is TagDepartment -> {
                                val tagDepartmentFull = TagDepartmentFull(
                                        it1.id,
                                        it1.name,
                                        it1.link,
                                        it1.tags,
                                        it.homeTags,
                                        it1.departmentColor
                                )
                                _tagDepartmentInfo.value = tagDepartmentFull
                            }
                            is ErrorResponse -> {
                                SingleEvent.triggerToast.value = Event(it1.message)
                                Timber.e(it1.message)
                            }
                        }
                    }, { it1 ->
                        Timber.e(it1)
                    })
        }
    }

    // Notice functions
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
        getNoticesOfDepartmentUseCase
                .getNotices(
                        tagDepartmentInfo.value!!.id,
                        paginationLimit,
                        paginationCursor,
                        homeTagString
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

    private fun updateNotices(postWork: () -> Unit = {}) {
        paginationCursor = null
        _isPageEnd.value = false
        getNoticesOfDepartmentUseCase
                .getNotices(
                        tagDepartmentInfo.value!!.id,
                        paginationLimit,
                        paginationCursor,
                        homeTagString
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
