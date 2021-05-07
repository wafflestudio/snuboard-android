package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.usecase.DeleteNoticeScrapUseCase
import com.wafflestudio.snuboard.domain.usecase.PostNoticeScrapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DepartmentHomeFragmentViewModel
@Inject
constructor(
        private val deleteNoticeScrapUseCase: DeleteNoticeScrapUseCase,
        private val postNoticeScrapUseCase: PostNoticeScrapUseCase,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {


}
