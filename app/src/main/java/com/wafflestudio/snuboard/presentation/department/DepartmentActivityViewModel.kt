package com.wafflestudio.snuboard.presentation.department

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.DepartmentColor
import com.wafflestudio.snuboard.domain.model.TagDepartment
import com.wafflestudio.snuboard.domain.usecase.DeleteFollowingTagUseCase
import com.wafflestudio.snuboard.domain.usecase.GetTagDepartmentInfoUseCase
import com.wafflestudio.snuboard.domain.usecase.PostFollowingTagUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentActivityViewModel
@Inject
constructor(
        private val getTagDepartmentInfoUseCase: GetTagDepartmentInfoUseCase,
        private val postFollowingTagUseCase: PostFollowingTagUseCase,
        private val deleteFollowingTagUseCase: DeleteFollowingTagUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _tagDepartmentInfo = MutableLiveData<TagDepartment>()

    val tagDepartmentInfo: LiveData<TagDepartment>
        get() = _tagDepartmentInfo

    fun getTagDepartmentInfo(departmentId: Int) {
        getTagDepartmentInfoUseCase
                .getTagDepartmentInfo(departmentId)
                .subscribe({
                    when (it) {
                        is TagDepartment -> {
                            _tagDepartmentInfo.value = it
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
                                _tagDepartmentInfo.value = it1
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

    fun changeDepartmentColor(departmentColor: DepartmentColor, pref: SharedPreferences) {
        val tmpTagDepartment = tagDepartmentInfo.value!!
        val preferenceKey = SharedPreferenceConst.getDepartmentKey(tmpTagDepartment.id)
        pref.edit {
            putInt(preferenceKey, departmentColor.colorId)
        }
        _tagDepartmentInfo.value = TagDepartment(
                tmpTagDepartment.id,
                tmpTagDepartment.name,
                tmpTagDepartment.tags,
                departmentColor
        )
    }

}