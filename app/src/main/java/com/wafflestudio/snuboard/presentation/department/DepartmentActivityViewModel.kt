package com.wafflestudio.snuboard.presentation.department

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DepartmentActivityViewModel
@Inject
constructor(
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}