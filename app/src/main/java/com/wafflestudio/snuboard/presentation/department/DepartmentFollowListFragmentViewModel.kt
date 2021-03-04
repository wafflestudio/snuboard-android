package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.EngDepartment
import com.wafflestudio.snuboard.domain.model.FollowingDepartment
import com.wafflestudio.snuboard.domain.usecase.ClassifyDepartmentUseCase
import com.wafflestudio.snuboard.utils.ErrorResponse
import com.wafflestudio.snuboard.utils.Event
import com.wafflestudio.snuboard.utils.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DepartmentFollowListFragmentViewModel
@Inject
constructor(
        private val classifyDepartmentUseCase: ClassifyDepartmentUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _followingDepartments = MutableLiveData<List<FollowingDepartment>>()
    private val _engDepartments = MutableLiveData<List<EngDepartment>>()
    val updateDepartments = classifyDepartmentUseCase.updateDepartments

    val followingDepartments: LiveData<List<FollowingDepartment>>
        get() = _followingDepartments
    val engDepartments: LiveData<List<EngDepartment>>
        get() = _engDepartments

    fun getDepartments() {
        val tmpFollowingDepartmentList = mutableListOf<FollowingDepartment>()
        val tmpEngDepartmentList = mutableListOf<EngDepartment>()
        classifyDepartmentUseCase
                .classifyDepartments()
                .subscribe({
                    when (it) {
                        is FollowingDepartment -> {
                            tmpFollowingDepartmentList.add(it)
                        }
                        is EngDepartment -> {
                            tmpEngDepartmentList.add(it)
                        }
                        is ErrorResponse -> {
                            SingleEvent.triggerToast.value = Event(it.message)
                            Timber.e(it.message)
                        }
                    }
                }, {
                    Timber.e(it)
                }, {
                    _followingDepartments.value = tmpFollowingDepartmentList
                    _engDepartments.value = tmpEngDepartmentList
                })
    }
}