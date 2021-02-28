package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.EngDepartment
import com.wafflestudio.snuboard.domain.model.FollowingDepartment
import com.wafflestudio.snuboard.domain.usecase.ClassifyDepartmentUseCase
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
        _followingDepartments.value = emptyList()
        _engDepartments.value = emptyList()
        classifyDepartmentUseCase
                .classifyDepartments()
                .subscribe({
                    when (it) {
                        is FollowingDepartment -> {
                            if (_followingDepartments.value == null)
                                _followingDepartments.value = listOf(it)
                            else {
                                val tmpList = _followingDepartments.value!!.toMutableList()
                                tmpList.add(it)
                                _followingDepartments.value = tmpList
                            }
                        }
                        is EngDepartment -> {
                            if (_engDepartments.value == null)
                                _engDepartments.value = listOf(it)
                            else {
                                val tmpList = _engDepartments.value!!.toMutableList()
                                tmpList.add(it)
                                _engDepartments.value = tmpList
                            }
                        }
                    }
                }, {
                    Timber.e(it)
                })
    }
}