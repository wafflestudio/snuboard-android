package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.CollegeDepartment
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.ART
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.CALS
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.CBA
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.CHE
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.CLS
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.EDU
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.ENGINEERING
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.HUMANITIES
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.MEDICINE
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.MUSIC
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.NURSING
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.PHARM
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.SCIENCE
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.SOCIAL
import com.wafflestudio.snuboard.domain.model.CollegeDepartment.Companion.VET
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


    val updateDepartments = classifyDepartmentUseCase.updateDepartments

    val followingDepartments: LiveData<List<FollowingDepartment>>
        get() = _followingDepartments


    private val _collegeDepartments = MutableLiveData<Map<String, List<CollegeDepartment>>>()
    val collegeDepartments: LiveData<Map<String, List<CollegeDepartment>>>
        get() = _collegeDepartments


    private val _isCollegeDepartmentsVisible = MutableLiveData(
            mapOf(
                    ENGINEERING to false,
                    HUMANITIES to false,
                    SOCIAL to false,
                    SCIENCE to false,
                    NURSING to false,
                    CBA to false,
                    CALS to false,
                    ART to false,
                    EDU to false,
                    CHE to false,
                    VET to false,
                    PHARM to false,
                    MUSIC to false,
                    MEDICINE to false,
                    CLS to false
            )
    )
    val isCollegeDepartmentsVisible: LiveData<Map<String, Boolean>>
        get() = _isCollegeDepartmentsVisible

    fun getDepartments() {
        val tmpFollowingDepartmentList = mutableListOf<FollowingDepartment>()
        val tmpCollegeDepartmentMap = mutableMapOf<String, List<CollegeDepartment>>()
        classifyDepartmentUseCase
                .classifyDepartments()
                .subscribe({
                    when (it) {
                        is FollowingDepartment -> {
                            tmpFollowingDepartmentList.add(it)
                        }
                        is CollegeDepartment -> {
                            var tmpDepartmentList = mutableListOf<CollegeDepartment>()
                            tmpCollegeDepartmentMap[it.college]?.let { it1 ->
                                tmpDepartmentList = it1.toMutableList()
                            }
                            tmpDepartmentList.add(it)
                            tmpCollegeDepartmentMap[it.college] = tmpDepartmentList
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
                    _collegeDepartments.value = tmpCollegeDepartmentMap
                })
    }

    fun toggleCollegeDepartment(collegeName: String) {
        val tmpIsCollegeDepartmentVisible = isCollegeDepartmentsVisible.value!!.toMutableMap()
        tmpIsCollegeDepartmentVisible[collegeName] = !tmpIsCollegeDepartmentVisible[collegeName]!!
        _isCollegeDepartmentsVisible.value = tmpIsCollegeDepartmentVisible
    }
}
