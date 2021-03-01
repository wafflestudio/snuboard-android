package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.TagDepartment
import com.wafflestudio.snuboard.domain.usecase.GetTagDepartmentInfoUseCase
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
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _departmentInfo = MutableLiveData<TagDepartment>()

    val departmentInfo: LiveData<TagDepartment>
        get() = _departmentInfo

    fun getDepartmentInfo(departmentId: Int) {
        getTagDepartmentInfoUseCase
                .getTagDepartmentInfo(departmentId)
                .subscribe({
                    when (it) {
                        is TagDepartment -> {
                            _departmentInfo.value = it
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