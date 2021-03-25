package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeSearchActivityViewModel
@Inject
constructor(
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}
