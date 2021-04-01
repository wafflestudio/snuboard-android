package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import com.wafflestudio.snuboard.domain.usecase.DeleteNoticeScrapUseCase
import com.wafflestudio.snuboard.domain.usecase.GetNoticesOfDepartmentUseCase
import com.wafflestudio.snuboard.domain.usecase.PostNoticeScrapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
}
