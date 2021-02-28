package com.wafflestudio.snuboard.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wafflestudio.snuboard.domain.model.Notice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoticeFeedFragmentViewModel
@Inject
constructor(
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _notices = MutableLiveData<List<Notice>>()
    val notices: LiveData<List<Notice>>
        get() = _notices
}